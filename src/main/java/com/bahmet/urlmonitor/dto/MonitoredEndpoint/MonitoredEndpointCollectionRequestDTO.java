package com.bahmet.urlmonitor.dto.MonitoredEndpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class MonitoredEndpointCollectionRequestDTO {

    @JsonProperty("monitored_endpoints")
    @Valid
    private List<MonitoredEndpointRequestDTO> monitoredEndpoints;
}
