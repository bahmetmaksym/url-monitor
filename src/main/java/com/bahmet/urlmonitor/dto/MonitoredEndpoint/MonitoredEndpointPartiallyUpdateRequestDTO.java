package com.bahmet.urlmonitor.dto.MonitoredEndpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitoredEndpointPartiallyUpdateRequestDTO {

    @Length(min = 1, max = 255, message = "Name should be between 1 and 255 characters")
    private String name;

    @URL(message = "The URL is invalid")
    private String url;

    @JsonProperty("monitored_interval")
    @Min(value = 60, message = "Monitored interval should be at least 60 seconds")
    private Long monitoredInterval;
}
