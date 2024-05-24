package com.bahmet.urlmonitor.dto.MonitoredEndpoint;

import com.bahmet.urlmonitor.dto.MonitoringResult.MonitoringResultResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MonitoredEndpointWithResultsResponseDTO {

    private Long id;

    private String name;

    private String url;

    @JsonProperty("date_of_creation")
    private LocalDateTime dateOfCreation;

    @JsonProperty("date_of_last_check")
    private LocalDateTime dateOfLastCheck;

    @JsonProperty("monitored_interval")
    private Long monitoredInterval;

    @JsonProperty("monitoring_results")
    private List<MonitoringResultResponseDTO> monitoringResults;
}
