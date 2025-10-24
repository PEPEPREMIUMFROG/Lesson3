package org.example.util;

import org.example.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        AppConfig config = new AppConfig();

        try (Connection connection = DriverManager.getConnection(
                config.getDatabaseUrl(),
                config.getDatabaseUsername(),
                config.getDatabasePassword());
             Statement statement = connection.createStatement()) {

            String schema = config.getDatabaseSchema();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schema);

            statement.execute("CREATE TABLE IF NOT EXISTS " + schema + ".network (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL UNIQUE, " +
                    "description TEXT, " +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS " + schema + ".device (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "network_id BIGINT NOT NULL REFERENCES " + schema + ".network(id) ON DELETE CASCADE, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "ip_address VARCHAR(15) NOT NULL, " +
                    "mac_address VARCHAR(17), " +
                    "type VARCHAR(50) NOT NULL, " +
                    "status VARCHAR(50) NOT NULL, " +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");");

            statement.execute("CREATE TABLE IF NOT EXISTS " + schema + ".connections (" +
                    "id BIGSERIAL PRIMARY KEY, " +
                    "device_from_id BIGINT NOT NULL REFERENCES " + schema + ".device(id) ON DELETE CASCADE, " +
                    "device_to_id BIGINT NOT NULL REFERENCES " + schema + ".device(id) ON DELETE CASCADE, " +
                    "connection_type VARCHAR(50) NOT NULL, " +
                    "status VARCHAR(50) NOT NULL, " +
                    "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                    ");");
            System.out.println("Database initialized successfully");

        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}