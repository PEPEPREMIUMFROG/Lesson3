package org.example.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionService {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try {
            Properties properties = new Properties();
            try (InputStream is = DatabaseConnectionService.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {
                properties.load(is);
                URL = properties.getProperty("db.url");
                USER = properties.getProperty("db.user");
                PASSWORD = properties.getProperty("db.password");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB config", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}