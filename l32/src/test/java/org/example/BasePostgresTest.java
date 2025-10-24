package org.example;

import org.example.config.AppConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

@Testcontainers
public abstract class BasePostgresTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    protected static AppConfig testConfig;

    @BeforeAll
    static void setupBase() throws Exception {
        testConfig = createTestConfig();
        initializeDatabaseSchema();
    }

    @AfterAll
    static void cleanup() {
    }

    protected static AppConfig createTestConfig() {
        return new AppConfig() {
            private final Properties testProperties = new Properties();
            {
                testProperties.setProperty("database.url", postgres.getJdbcUrl());
                testProperties.setProperty("database.username", postgres.getUsername());
                testProperties.setProperty("database.password", postgres.getPassword());
                testProperties.setProperty("database.schema", "networks");
            }

            @Override
            public String getDatabaseUrl() {
                return testProperties.getProperty("database.url");
            }

            @Override
            public String getDatabaseUsername() {
                return testProperties.getProperty("database.username");
            }

            @Override
            public String getDatabasePassword() {
                return testProperties.getProperty("database.password");
            }

            @Override
            public String getDatabaseSchema() {
                return testProperties.getProperty("database.schema");
            }
        };
    }

    protected static void initializeDatabaseSchema() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE SCHEMA IF NOT EXISTS networks;");

            statement.execute("""
                CREATE TABLE IF NOT EXISTS networks.network (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    description TEXT,
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS networks.device (
                    id BIGSERIAL PRIMARY KEY,
                    network_id BIGINT NOT NULL REFERENCES networks.network(id) ON DELETE CASCADE,
                    name VARCHAR(255) NOT NULL,
                    ip_address VARCHAR(15) NOT NULL,
                    mac_address VARCHAR(17),
                    type VARCHAR(50) NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS networks.connections (
                    id BIGSERIAL PRIMARY KEY,
                    device_from_id BIGINT NOT NULL REFERENCES networks.device(id) ON DELETE CASCADE,
                    device_to_id BIGINT NOT NULL REFERENCES networks.device(id) ON DELETE CASCADE,
                    connection_type VARCHAR(50) NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);
        }
    }

    protected void clearDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword());
             Statement statement = connection.createStatement()) {

            statement.execute("TRUNCATE TABLE networks.connections CASCADE");
            statement.execute("TRUNCATE TABLE networks.device CASCADE");
            statement.execute("TRUNCATE TABLE networks.network CASCADE");
        }
    }
}