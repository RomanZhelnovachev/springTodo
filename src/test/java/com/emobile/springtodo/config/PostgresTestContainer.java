package com.emobile.springtodo.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class PostgresTestContainer {

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testDb")
            .withUsername("testUser")
            .withPassword("test");

    static {
        container.start();
    }
}
