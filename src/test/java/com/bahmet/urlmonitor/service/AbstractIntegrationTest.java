package com.bahmet.urlmonitor.service;


import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0")
            .withDatabaseName("UrlMonitorTest")
            .withUsername("testroot")
            .withPassword("testadmin");

    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2.5")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @AfterAll
    static void cleanup() {
        mySQLContainer.stop();
        redisContainer.stop();
    }
}


