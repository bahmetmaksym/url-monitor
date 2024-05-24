package com.bahmet.urlmonitor.controller;

import com.bahmet.urlmonitor.dto.MonitoredEndpoint.*;
import com.bahmet.urlmonitor.service.MonitoredEndpointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class MonitoredEndpointController {

    private final MonitoredEndpointService monitoredEndpointService;

    @GetMapping("/monitored-endpoints")
    @Operation(summary = "Get all endpoints", description = "Get all monitored endpoints for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "List of monitored endpoints", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<MonitoredEndpointResponseDTO>> getAllMonitoredEndpoints(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        log.info("Fetching all monitored endpoints for user: {}", userId);

        List<MonitoredEndpointResponseDTO> endpoints = monitoredEndpointService.getAllMonitoredEndpoints(userId);

        log.info("Found {} monitored endpoints for user: {}", endpoints.size(), userId);

        return ResponseEntity.ok(endpoints);
    }

    @GetMapping("/monitored-endpoints/{id}")
    @Operation(summary = "Get endpoint by ID", description = "Get monitored endpoint by ID for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Monitored endpoint", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitored endpoint not found", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MonitoredEndpointWithResultsResponseDTO> getMonitoredEndpointById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        String userId = jwt.getSubject();

        log.info("Fetching monitored endpoint with ID: {} for user: {}", id, userId);

        MonitoredEndpointWithResultsResponseDTO endpoint = monitoredEndpointService.getMonitoredEndpointById(id, userId);

        log.info("Fetched monitored endpoint with ID: {} for user: {}", id, userId);

        return ResponseEntity.ok(endpoint);
    }

    @PostMapping("/monitored-endpoints")
    @Operation(summary = "Create endpoint", description = "Create monitored endpoint for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Created monitored endpoint", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MonitoredEndpointResponseDTO> createMonitoredEndpoint(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid MonitoredEndpointRequestDTO monitoredEndpoint) {
        String userId = jwt.getSubject();

        log.info("Creating monitored endpoint for user: {}", userId);

        MonitoredEndpointResponseDTO createdEndpoint = monitoredEndpointService.createMonitoredEndpoint(monitoredEndpoint, userId);

        log.info("Created monitored endpoint with ID: {} for user: {}", createdEndpoint.getId(), userId);

        return ResponseEntity.ok(createdEndpoint);
    }

    @PostMapping("/monitored-endpoints-collection")
    @Operation(summary = "Create endpoints collection", description = "Create monitored endpoints collection for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Created monitored endpoints collection", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> createMonitoredEndpointCollection(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid MonitoredEndpointCollectionRequestDTO monitoredEndpoint) {
        String userId = jwt.getSubject();

        log.info("Creating monitored endpoints collection for user: {}", userId);

        monitoredEndpointService.createMonitoredEndpointCollection(monitoredEndpoint, userId);

        log.info("Created monitored endpoints collection for user: {}", userId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/monitored-endpoints/{id}")
    @Operation(summary = "Update endpoint", description = "Update monitored endpoint by ID for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Updated monitored endpoint", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitored endpoint not found", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MonitoredEndpointResponseDTO> updateMonitoredEndpoint(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody @Valid MonitoredEndpointRequestDTO monitoredEndpoint) {
        String userId = jwt.getSubject();

        log.info("Updating monitored endpoint with ID: {} for user: {}", id, userId);

        MonitoredEndpointResponseDTO updatedEndpoint = monitoredEndpointService.updateMonitoredEndpoint(id, monitoredEndpoint, userId);

        log.info("Updated monitored endpoint with ID: {} for user: {}", id, userId);

        return ResponseEntity.ok(updatedEndpoint);
    }

    @PatchMapping("/monitored-endpoints/{id}")
    @Operation(summary = "Partially update endpoint", description = "Partially update monitored endpoint by ID for the authenticated user.", responses = {
            @ApiResponse(responseCode = "200", description = "Partially updated monitored endpoint", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitored endpoint not found", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<MonitoredEndpointResponseDTO> updateMonitoredEndpointPartially(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody @Valid MonitoredEndpointPartiallyUpdateRequestDTO monitoredEndpoint) {
        String userId = jwt.getSubject();

        log.info("Partially updating monitored endpoint with ID: {} for user: {}", id, userId);

        MonitoredEndpointResponseDTO updatedEndpoint = monitoredEndpointService.updateMonitoredEndpointPartially(id, monitoredEndpoint, userId);

        log.info("Partially updated monitored endpoint with ID: {} for user: {}", id, userId);

        return ResponseEntity.ok(updatedEndpoint);
    }

    @DeleteMapping("/monitored-endpoints/{id}")
    @Operation(summary = "Delete endpoint", description = "Delete monitored endpoint by ID for the authenticated user.", responses = {
            @ApiResponse(responseCode = "204", description = "Monitored endpoint deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Monitored endpoint not found", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<Void> deleteMonitoredEndpoint(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        String userId = jwt.getSubject();

        log.info("Deleting monitored endpoint with ID: {} for user: {}", id, userId);

        monitoredEndpointService.deleteMonitoredEndpoint(id, userId);

        log.info("Deleted monitored endpoint with ID: {} for user: {}", id, userId);

        return ResponseEntity.noContent().build();
    }
}
