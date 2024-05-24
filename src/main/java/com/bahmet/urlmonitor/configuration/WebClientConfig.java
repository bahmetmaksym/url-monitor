package com.bahmet.urlmonitor.configuration;

import com.bahmet.urlmonitor.dto.configuration.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClientProperties webClientProperties;

    @Bean
    public WebClient webClient() {
        ConnectionProvider provider = ConnectionProvider.builder(webClientProperties.getConnectionProvider().getName())
                .maxConnections(webClientProperties.getConnectionProvider().getMaxConnections())
                .maxIdleTime(Duration.ofSeconds(webClientProperties.getConnectionProvider().getMaxIdleTime()))
                .maxLifeTime(Duration.ofSeconds(webClientProperties.getConnectionProvider().getMaxLifeTime()))
                .pendingAcquireTimeout(Duration.ofSeconds(webClientProperties.getConnectionProvider().getPendingAcquireTimeout()))
                .pendingAcquireMaxCount(webClientProperties.getConnectionProvider().getPendingAcquireMaxCount())
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .responseTimeout(Duration.ofSeconds(webClientProperties.getHttpClient().getResponseTimeout()));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
