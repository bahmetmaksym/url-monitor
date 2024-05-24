package com.bahmet.urlmonitor.repository;

import com.bahmet.urlmonitor.repository.model.MonitoringResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {
    List<MonitoringResult> findTop10ByMonitoredEndpointIdOrderByDateOfCheckDesc(Long monitoredEndpointId);
}
