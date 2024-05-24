package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.configuration.TimeConfig;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class MonitoringSchedulerService {

    private final MonitoredEndpointService monitoredEndpointService;

    private final HttpClientService httpClientService;

    private final TimeConfig.LocalDateTimeProvider localDateTimeProvider;

    @Async
    @Scheduled(fixedDelayString = "${urlmonitor.scheduling.interval}")
    public void checkMonitoredEndpoints() {
        log.info("Checking monitored endpoints");

        LocalDateTime checkDateTime = localDateTimeProvider.getLocalDateTime();

        List<MonitoredEndpoint> endpointsToCheck = monitoredEndpointService.getEndpointsToCheck(checkDateTime);

        httpClientService.monitorEndpoints(endpointsToCheck, checkDateTime);
    }
}
