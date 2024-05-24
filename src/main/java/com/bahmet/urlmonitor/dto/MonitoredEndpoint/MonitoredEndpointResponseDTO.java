package com.bahmet.urlmonitor.dto.MonitoredEndpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitoredEndpointResponseDTO {
    private Long id;

    private String name;

    private String url;

    @JsonProperty("date_of_creation")
    private LocalDateTime dateOfCreation;

    @JsonProperty("date_of_last_check")
    private LocalDateTime dateOfLastCheck;

    @JsonProperty("monitored_interval")
    private Long monitoredInterval;
}
