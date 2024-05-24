package com.bahmet.urlmonitor.dto.MonitoredEndpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class MonitoredEndpointRequestDTO {

    @NotBlank(message = "Name is mandatory")
    @Length(min = 1, max = 255, message = "Name should be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "URL is mandatory")
    @URL(message = "The URL is invalid")
    private String url;

    @JsonProperty("monitored_interval")
    @Min(value = 60, message = "Monitored interval should be at least 60 seconds")
    private Long monitoredInterval;
}
