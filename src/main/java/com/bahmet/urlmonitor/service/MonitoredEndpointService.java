package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.dto.MonitoredEndpoint.*;
import com.bahmet.urlmonitor.exception.MonitoredEndpointNotFoundException;
import com.bahmet.urlmonitor.repository.MonitoringResultRepository;
import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import com.bahmet.urlmonitor.repository.MonitoredEndpointRepository;
import com.bahmet.urlmonitor.repository.model.MonitoringResult;
import com.bahmet.urlmonitor.util.mapper.MonitoredEndpointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MonitoredEndpointService {

    private final MonitoredEndpointRepository monitoredEndpointRepository;

    private final MonitoringResultRepository monitoringResultRepository;

    private final MonitoredEndpointMapper monitoredEndpointMapper;

    private final DistributedLockService distributedLockService;


    public List<MonitoredEndpointResponseDTO> getAllMonitoredEndpoints(String userId) {
        return monitoredEndpointMapper.toResponseDTO(monitoredEndpointRepository.findByUserId(userId));
    }


    public MonitoredEndpointWithResultsResponseDTO getMonitoredEndpointById(Long id, String userId) {
        MonitoredEndpoint monitoredEndpoint = monitoredEndpointRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new MonitoredEndpointNotFoundException("Monitored endpoint not found"));

        List<MonitoringResult> monitoringResults = monitoringResultRepository.findTop10ByMonitoredEndpointIdOrderByDateOfCheckDesc(id);

        monitoredEndpoint.setMonitoringResults(monitoringResults);

        return monitoredEndpointMapper.toResponseDTOWithResults(monitoredEndpoint);
    }


    @Transactional
    public MonitoredEndpointResponseDTO createMonitoredEndpoint(MonitoredEndpointRequestDTO monitoredEndpointRequestDTO, String userId) {
        MonitoredEndpoint monitoredEndpoint = monitoredEndpointMapper.toEntity(monitoredEndpointRequestDTO);

        monitoredEndpoint.setUserId(userId);

        monitoredEndpoint = monitoredEndpointRepository.save(monitoredEndpoint);

        return monitoredEndpointMapper.toResponseDTO(monitoredEndpoint);
    }


    @Transactional
    public void createMonitoredEndpointCollection(MonitoredEndpointCollectionRequestDTO monitoredEndpointCollectionRequestDTO, String userId) {
        List<MonitoredEndpoint> monitoredEndpoint = monitoredEndpointMapper.toEntity(monitoredEndpointCollectionRequestDTO.getMonitoredEndpoints());

        monitoredEndpoint.forEach(monitoredEndpoint1 -> monitoredEndpoint1.setUserId(userId));

        monitoredEndpointRepository.saveAll(monitoredEndpoint);
    }


    @Transactional
    public MonitoredEndpointResponseDTO updateMonitoredEndpoint(Long id, MonitoredEndpointRequestDTO monitoredEndpointRequestDTO, String userId) {
        MonitoredEndpoint monitoredEndpointToUpdate = monitoredEndpointRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new MonitoredEndpointNotFoundException("Monitored endpoint not found"));

        updateMonitoredEndpointFields(monitoredEndpointToUpdate, monitoredEndpointRequestDTO);

        MonitoredEndpoint updatedMonitoredEndpoint = monitoredEndpointRepository.save(monitoredEndpointToUpdate);

        return monitoredEndpointMapper.toResponseDTO(updatedMonitoredEndpoint);
    }


    @Transactional
    public MonitoredEndpointResponseDTO updateMonitoredEndpointPartially(Long id, MonitoredEndpointPartiallyUpdateRequestDTO monitoredEndpointPartiallyUpdateRequestDTO, String userId) {
        if (isDtoEmpty(monitoredEndpointPartiallyUpdateRequestDTO)) {
            throw new RuntimeException("Request body is empty");
        }

        MonitoredEndpoint monitoredEndpoint = monitoredEndpointRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new MonitoredEndpointNotFoundException("Monitored endpoint not found"));

        updateMonitoredEndpointFields(monitoredEndpoint, monitoredEndpointPartiallyUpdateRequestDTO);

        MonitoredEndpoint updatedMonitoredEndpoint = monitoredEndpointRepository.save(monitoredEndpoint);

        return monitoredEndpointMapper.toResponseDTO(updatedMonitoredEndpoint);
    }


    @Transactional
    public void deleteMonitoredEndpoint(Long id, String userId) {
        Optional<MonitoredEndpoint> monitoredEndpointOptional = monitoredEndpointRepository.findByIdAndUserId(id, userId);

        monitoredEndpointOptional.ifPresentOrElse(monitoredEndpointRepository::delete, () -> {
            throw new MonitoredEndpointNotFoundException("Monitored endpoint not found");
        });
    }


    public List<MonitoredEndpoint> getEndpointsToCheck(LocalDateTime checkDateTime) {
        Set<String> lockedUrlKeys = distributedLockService.getLockedEndpointsKeys();

        List<Long> lockedEndpointsIds = distributedLockService.parseLockedEndpointIds(lockedUrlKeys);

        if (lockedEndpointsIds.isEmpty()) {
            return monitoredEndpointRepository.findEndpointsToCheck(checkDateTime);
        } else {
            return monitoredEndpointRepository.findEndpointsToCheck(checkDateTime, lockedEndpointsIds);
        }
    }


    private void updateMonitoredEndpointFields(MonitoredEndpoint monitoredEndpoint, MonitoredEndpointRequestDTO monitoredEndpointParticularUpdateRequestDTO) {
        monitoredEndpoint.setName(monitoredEndpointParticularUpdateRequestDTO.getName());

        monitoredEndpoint.setUrl(monitoredEndpointParticularUpdateRequestDTO.getUrl());

        monitoredEndpoint.setMonitoredInterval(monitoredEndpointParticularUpdateRequestDTO.getMonitoredInterval());
    }


    private void updateMonitoredEndpointFields(MonitoredEndpoint monitoredEndpoint, MonitoredEndpointPartiallyUpdateRequestDTO monitoredEndpointPartiallyUpdateRequestDTO) {
        if (monitoredEndpointPartiallyUpdateRequestDTO.getName() != null) {
            monitoredEndpoint.setName(monitoredEndpointPartiallyUpdateRequestDTO.getName());
        }

        if (monitoredEndpointPartiallyUpdateRequestDTO.getUrl() != null) {
            monitoredEndpoint.setUrl(monitoredEndpointPartiallyUpdateRequestDTO.getUrl());
        }

        if (monitoredEndpointPartiallyUpdateRequestDTO.getMonitoredInterval() != null) {
            monitoredEndpoint.setMonitoredInterval(monitoredEndpointPartiallyUpdateRequestDTO.getMonitoredInterval());
        }
    }


    private boolean isDtoEmpty(MonitoredEndpointPartiallyUpdateRequestDTO dto) {
        return dto.getName() == null && dto.getUrl() == null && dto.getMonitoredInterval() == null;
    }
}
