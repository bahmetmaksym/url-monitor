package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.configuration.TimeConfig;
import com.bahmet.urlmonitor.dto.MonitoredEndpoint.MonitoredEndpointRequestDTO;
import com.bahmet.urlmonitor.dto.MonitoredEndpoint.MonitoredEndpointResponseDTO;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import com.bahmet.urlmonitor.repository.model.MonitoringResult;
import com.bahmet.urlmonitor.repository.MonitoredEndpointRepository;
import com.bahmet.urlmonitor.repository.MonitoringResultRepository;
import com.bahmet.urlmonitor.util.mapper.MonitoredEndpointMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonitoringResultServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MonitoringResultService monitoringResultService;

    @Autowired
    private MonitoringResultRepository monitoringResultRepository;

    @Autowired
    private MonitoredEndpointRepository monitoredEndpointRepository;

    @Autowired
    private MonitoredEndpointService monitoredEndpointService;

    @Autowired
    private TimeConfig.LocalDateTimeProvider localDateTimeProvider;

    @Autowired
    private MonitoredEndpointMapper monitoredEndpointMapper;

    @Test
    void whenSaveMonitoringResult_thenDateOfLastCheckUpdated() {
        // Arrange
        String userId = "1";
        MonitoredEndpointRequestDTO endpointRequest = new MonitoredEndpointRequestDTO();
        endpointRequest.setName("Test Endpoint");
        endpointRequest.setUrl("http://example.com");
        endpointRequest.setMonitoredInterval(60L);

        MonitoredEndpointResponseDTO savedEndpointResponse = monitoredEndpointService.createMonitoredEndpoint(endpointRequest, userId);
        MonitoredEndpoint savedEndpoint = monitoredEndpointMapper.toEntity(savedEndpointResponse);

        LocalDateTime checkDateTime = localDateTimeProvider.getLocalDateTime();

        // Act
        monitoringResultService.saveMonitoringResult(savedEndpoint, 200, "Test payload", checkDateTime);

        // Assert
        MonitoredEndpoint updatedEndpoint = monitoredEndpointRepository.findById(savedEndpoint.getId())
                .orElseThrow(() -> new AssertionError("Endpoint should exist"));
        assertEquals(checkDateTime.truncatedTo(ChronoUnit.MILLIS), updatedEndpoint.getDateOfLastCheck().truncatedTo(ChronoUnit.MILLIS), "dateOfLastCheck should be updated to the latest check time");

        List<MonitoringResult> results = monitoringResultRepository.findAll();
        assertTrue(results.stream()
                        .anyMatch(result -> result.getMonitoredEndpoint().getId().equals(savedEndpoint.getId()) &&
                                result.getDateOfCheck().equals(checkDateTime) &&
                                result.getHttpStatusCode() == 200 &&
                                result.getPayload().equals("Test payload")),
                "Monitoring result should be saved with the correct details");
    }
}

