package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.configuration.TimeConfig;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import com.bahmet.urlmonitor.repository.MonitoredEndpointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class MonitoredEndpointServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MonitoredEndpointService monitoredEndpointService;

    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;

    @Autowired
    private DistributedLockService distributedLockService;

    @Autowired
    private TimeConfig.LocalDateTimeProvider localDateTimeProvider;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @AfterEach
    void cleanUp() {
        monitoredEndpointRepository.deleteAll();
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("lock:monitored-endpoint:*")));
    }

    @Test
    void whenDatabaseIsEmpty_thenNoEndpointsToCheck() {
        // Act
        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(localDateTimeProvider.getLocalDateTime());

        // Assert
        assertTrue(endpointsToCheck.isEmpty(), "There should be no endpoints to check when the database is empty");
    }

    @Test
    void whenEndpointsNeedCheck_thenReturned() {
        // Arrange
        LocalDateTime now = localDateTimeProvider.getLocalDateTime();
        MonitoredEndpoint endpoint = createMonitoredEndpoint("http://example.com", now);

        // Act
        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(now.plusSeconds(60));

        // Assert
        assertTrue(endpointsToCheck.stream().anyMatch(e -> e.getId().equals(endpoint.getId())), "Endpoint should be returned when it needs to be checked");
    }

    @Test
    void whenAllKeysLocked_thenNoEndpointsToCheck() {
        // Arrange
        LocalDateTime now = localDateTimeProvider.getLocalDateTime();
        MonitoredEndpoint endpoint = createMonitoredEndpoint("http://example.com", now);

        distributedLockService.lockEndpoint(endpoint);

        // Act
        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(now.plusSeconds(60));

        // Assert
        assertTrue(endpointsToCheck.isEmpty(), "No endpoints should be returned when all keys are locked");
    }

    @Test
    void whenSomeKeysLocked_thenUnlockedEndpointsReturned() {
        // Arrange
        LocalDateTime now = localDateTimeProvider.getLocalDateTime();
        MonitoredEndpoint endpoint1 = createMonitoredEndpoint("http://example1.com", now);
        MonitoredEndpoint endpoint2 = createMonitoredEndpoint("http://example2.com", now);

        distributedLockService.lockEndpoint(endpoint1);

        // Act
        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(now.plusSeconds(60));

        // Assert
        assertTrue(endpointsToCheck.stream().anyMatch(e -> e.getId().equals(endpoint2.getId())), "Unlocked endpoints should be returned");
    }

    @Test
    void whenLockReleased_thenEndpointsReturned() {
        // Arrange
        LocalDateTime now = localDateTimeProvider.getLocalDateTime();
        MonitoredEndpoint endpoint = createMonitoredEndpoint("http://example.com", now);

        distributedLockService.lockEndpoint(endpoint);
        distributedLockService.releaseLock(endpoint);

        // Act
        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(now.plusSeconds(60));

        // Assert
        assertTrue(endpointsToCheck.stream().anyMatch(e -> e.getId().equals(endpoint.getId())), "Endpoint should be returned after the lock is released");
    }

    private MonitoredEndpoint createMonitoredEndpoint(String url, LocalDateTime now) {
        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setName("Test Endpoint");
        endpoint.setUrl(url);
        endpoint.setMonitoredInterval(60L);
        endpoint.setUserId("user123");
        endpoint.setDateOfCreation(now);
        endpoint.setDateOfLastCheck(now);
        return monitoredEndpointRepository.save(endpoint);
    }
}

