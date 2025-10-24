package org.example;

import org.example.config.AppConfig;
import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.Network;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NetworkDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    private NetworkDao networkDao;
    private AppConfig testConfig;

    @BeforeAll
    void setup() throws Exception {
        testConfig = createTestConfig();
        initializeDatabaseSchema();
        networkDao = new NetworkDao(testConfig);
    }

    private AppConfig createTestConfig() {
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

    private void initializeDatabaseSchema() throws Exception {
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
                    network_id BIGINT NOT NULL REFERENCES networks.network(id),
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
                    device_from_id BIGINT NOT NULL REFERENCES networks.device(id),
                    device_to_id BIGINT NOT NULL REFERENCES networks.device(id),
                    connection_type VARCHAR(50) NOT NULL,
                    status VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """);
        }
    }

    @Test
    void testNetworkCreation() throws Exception {
        Network network = new Network("Test Network", "Test Description");
        Network savedNetwork = networkDao.save(network);
        assertNotNull(savedNetwork.getId());
        assertEquals("Test Network", savedNetwork.getName());
        assertEquals("Test Description", savedNetwork.getDescription());
        assertNotNull(savedNetwork.getCreatedAt());
    }

    @Test
    void testDeviceCreation() throws Exception {
        Network network = new Network("Device Test Network", "For device testing");
        Network savedNetwork = networkDao.save(network);
        Device device = new Device("Test Router", "192.168.1.1", "00:1B:44:11:3A:B7", "Router", "active");
        device.setNetworkId(savedNetwork.getId());
        Device savedDevice = networkDao.save(device);
        assertNotNull(savedDevice.getId());
        assertEquals(savedNetwork.getId(), savedDevice.getNetworkId());
        assertEquals("Test Router", savedDevice.getName());
    }

    @Test
    void testFindNetworksByName() throws Exception {
        Network network1 = new Network("SearchTest Network", "For search testing");
        Network network2 = new Network("Another Network", "Another description");
        networkDao.save(network1);
        networkDao.save(network2);
        var results = networkDao.findNetworksByName("SearchTest");
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("SearchTest Network", results.get(0).getName());
    }
}

