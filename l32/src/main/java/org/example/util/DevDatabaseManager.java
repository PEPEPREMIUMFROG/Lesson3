package org.example.util;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class DevDatabaseManager {
    private static PostgreSQLContainer<?> container;

    public static void start() {
        container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                .withDatabaseName("networks_db")
                .withUsername("admin")
                .withPassword("admin");
        container.start();
    }

    public static String getJdbcUrl() {
        return container.getJdbcUrl();
    }

    public static String getUsername() {
        return container.getUsername();
    }

    public static String getPassword() {
        return container.getPassword();
    }
}