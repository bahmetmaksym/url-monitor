package com.bahmet.urlmonitor.dto.MonitoringResult;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitoringResultResponseDTO {

    private Long id;

    @JsonProperty("date_of_check")
    private LocalDateTime dateOfCheck;

    @JsonProperty("http_status_code")
    private Integer httpStatusCode;

    @JsonProperty("payload")
    private String payload;
}
