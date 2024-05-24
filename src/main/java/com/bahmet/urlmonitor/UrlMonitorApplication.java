package com.bahmet.urlmonitor;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@OpenAPIDefinition
public class UrlMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlMonitorApplication.class, args);
    }

}
