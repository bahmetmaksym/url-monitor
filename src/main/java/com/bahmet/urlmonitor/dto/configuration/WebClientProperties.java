package com.bahmet.urlmonitor.dto.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "urlmonitor.webclient")
@Data
public class WebClientProperties {

    private ConnectionProviderProperties connectionProvider;

    private HttpClientProperties httpClient;


    @Data
    public static class ConnectionProviderProperties {
        private String name;

        private Integer maxConnections;

        private Integer maxIdleTime;

        private Integer maxLifeTime;

        private Integer pendingAcquireTimeout;

        private Integer pendingAcquireMaxCount;
    }

    @Data
    public static class HttpClientProperties {
        private Integer responseTimeout;
    }

}
