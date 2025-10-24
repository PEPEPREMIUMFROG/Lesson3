package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private final Properties properties;

    public AppConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading application properties", e);
        }
    }

    public String getDatabaseUrl() {
        return System.getProperty("db.url", properties.getProperty("database.url"));
    }
    public String getDatabaseUsername() {
        return System.getProperty("db.username", properties.getProperty("database.username"));
    }
    public String getDatabasePassword() {
        return System.getProperty("db.password", properties.getProperty("database.password"));
    }

    public String getDatabaseSchema() {
        return properties.getProperty("database.schema");
    }

}