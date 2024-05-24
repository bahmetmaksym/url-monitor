package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.dto.MonitoringResult.MonitoringResultResponseDTO;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import com.bahmet.urlmonitor.repository.model.MonitoringResult;
import com.bahmet.urlmonitor.repository.MonitoredEndpointRepository;
import com.bahmet.urlmonitor.repository.MonitoringResultRepository;
import com.bahmet.urlmonitor.util.mapper.MonitoringResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringResultService {

    private final MonitoringResultRepository monitoringResultRepository;

    private final MonitoredEndpointRepository monitoredEndpointRepository;

    private final MonitoringResultMapper monitoringResultMapper;

    public MonitoringResultResponseDTO saveMonitoringResult(MonitoredEndpoint endpoint, int statusCode, String payload, LocalDateTime monitoringDateTime) {
        MonitoringResult monitoringResult = new MonitoringResult();

        monitoringResult.setMonitoredEndpoint(endpoint);
        monitoringResult.setDateOfCheck(monitoringDateTime);
        monitoringResult.setHttpStatusCode(statusCode);
        monitoringResult.setPayload(payload);

        monitoringResultRepository.save(monitoringResult);

        endpoint.setDateOfLastCheck(monitoringDateTime);

        monitoredEndpointRepository.save(endpoint);

        log.info("Monitoring result saved for endpoint: {}", endpoint.getId());

        return monitoringResultMapper.toResponseDTO(monitoringResult);
    }
}
