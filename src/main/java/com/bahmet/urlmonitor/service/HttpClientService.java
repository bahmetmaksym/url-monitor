package com.bahmet.urlmonitor.service;

import com.bahmet.urlmonitor.repository.model.MonitoredEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpClientService {

    private static final long PAYLOAD_SIZE_LIMIT = 1024 * 1024;

    private final DistributedLockService distributedLockService;

    private final WebClient webClient;

    private final MonitoringResultService monitoringResultService;

    public void monitorEndpoints(List<MonitoredEndpoint> endpointsToCheck, LocalDateTime checkDateTime) {
        endpointsToCheck.parallelStream()
                .filter(distributedLockService::lockEndpoint)
                .forEach(endpoint -> checkEndpoint(endpoint, checkDateTime));
    }

    private void checkEndpoint(MonitoredEndpoint endpoint, LocalDateTime checkDateTime) {
        performUrlCheckAsync(endpoint, checkDateTime)
                .doFinally(signalType -> {
                    log.info("Releasing lock for endpoint: {}", endpoint.getId());
                    distributedLockService.releaseLock(endpoint);
                })
                .subscribe();
    }

    private Mono<Void> performUrlCheckAsync(MonitoredEndpoint endpoint, LocalDateTime checkDateTime) {
        log.info("Performing URL check for endpoint: {} at {}", endpoint.getId(), LocalDateTime.now());
        return webClient.get()
                .uri(endpoint.getUrl())
                .exchangeToMono(response -> handleResponse(response, endpoint, checkDateTime))
                .timeout(Duration.ofSeconds(1))
                .onErrorResume(error -> logErrorAndRelease(endpoint, error, checkDateTime))
                .doOnSuccess(result -> log.info("Request successful for endpoint: {}", endpoint.getId()))
                .doOnError(error -> log.error("Request error for endpoint: {}: {}", endpoint.getId(), error.getMessage()));
    }

    private Mono<Void> handleResponse(ClientResponse response, MonitoredEndpoint endpoint, LocalDateTime checkDateTime) {
        AtomicLong byteCount = new AtomicLong();

        return response.bodyToFlux(DataBuffer.class)
                .takeWhile(dataBuffer -> {
                    long currentCount = byteCount.addAndGet(dataBuffer.readableByteCount());
                    boolean isWithinLimit = currentCount <= PAYLOAD_SIZE_LIMIT;
                    if (!isWithinLimit) {
                        DataBufferUtils.release(dataBuffer);
                    }
                    return isWithinLimit;
                })
                .map(dataBuffer -> {
                    String result = dataBuffer.toString(StandardCharsets.UTF_8);
                    DataBufferUtils.release(dataBuffer);
                    return result;
                })
                .reduce(String::concat)
                .doOnNext(payload -> monitoringResultService.saveMonitoringResult(endpoint, response.statusCode().value(), payload, checkDateTime))
                .then();
    }

    private Mono<Void> logErrorAndRelease(MonitoredEndpoint endpoint, Throwable error, LocalDateTime checkDateTime) {
        log.error("Error during URL check for endpoint: {}: {}", endpoint.getId(), error.getMessage());

        monitoringResultService.saveMonitoringResult(endpoint, 500, error.getMessage(), checkDateTime);

        return Mono.empty();
    }
}
