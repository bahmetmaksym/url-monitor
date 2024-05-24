package com.bahmet.urlmonitor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class TimeConfig {

    @Bean
    public LocalDateTimeProvider localDateTimeProvider() {
        return LocalDateTime::now;
    }

    @FunctionalInterface
    public interface LocalDateTimeProvider {
        LocalDateTime getLocalDateTime();
    }
}
