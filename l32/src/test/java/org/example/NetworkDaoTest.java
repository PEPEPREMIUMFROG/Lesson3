package org.example;

import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.Network;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NetworkDaoTest extends BasePostgresTest {

    private NetworkDao networkDao;

    @BeforeEach
    void setUp() throws Exception {
        clearDatabase();
        networkDao = new NetworkDao(testConfig);
    }

    @Test
    void testNetworkCreate() throws Exception {
        Network network = new Network("testName", "testDescription");
        Network savedNetwork = networkDao.save(network);
        assertNotNull(savedNetwork.getId());
        assertEquals("testName", savedNetwork.getName());
        assertNotNull(savedNetwork.getCreatedAt());
    }

    @Test
    void testGetAllNetworks() throws Exception {
        Network network1 = new Network("Network1", "Description1");
        Network network2 = new Network("Network2", "Description2");
        networkDao.save(network1);
        networkDao.save(network2);
        List<Network> networks = networkDao.getAllNetworks();
        assertEquals(2, networks.size());
        assertTrue(networks.stream().anyMatch(n -> "Network1".equals(n.getName())));
        assertTrue(networks.stream().anyMatch(n -> "Network2".equals(n.getName())));
    }

    @Test
    void testDeviceCreation() throws Exception {
        Network network = new Network("Test Network", "For devices");
        Network savedNetwork = networkDao.save(network);
        Device device = new Device("Router1", "192.168.1.1", "00:1B:44:11:3A:B7", "Router", "active");
        device.setNetworkId(savedNetwork.getId());
        Device savedDevice = networkDao.save(device);
        assertNotNull(savedDevice.getId());
        assertEquals(savedNetwork.getId(), savedDevice.getNetworkId());
        assertEquals("Router1", savedDevice.getName());
    }

    @Test
    void testFindNetworksByName() throws Exception {
        networkDao.save(new Network("Production Network", "Prod env"));
        networkDao.save(new Network("Development Network", "Dev env"));
        networkDao.save(new Network("Test Network", "Test env"));
        List<Network> results = networkDao.findNetworksByName("Test");
        assertEquals(1, results.size());
        assertEquals("Test Network", results.get(0).getName());
    }

    @Test
    void testNetworkStatistics() throws Exception {
        Network network = new Network("Stats Network", "For statistics");
        Network savedNetwork = networkDao.save(network);
        Device device1 = new Device("Router1", "192.168.1.1", "00:1B:44:11:3A:B7", "Router", "active");
        device1.setNetworkId(savedNetwork.getId());
        networkDao.save(device1);
        Device device2 = new Device("Switch1", "192.168.1.2", "00:1B:44:11:3A:B8", "Switch", "active");
        device2.setNetworkId(savedNetwork.getId());
        networkDao.save(device2);
        Device device3 = new Device("Server1", "192.168.1.3", "00:1B:44:11:3A:B9", "Server", "inactive");
        device3.setNetworkId(savedNetwork.getId());
        networkDao.save(device3);
        var stats = networkDao.getNetworkStatistics(savedNetwork.getId());
        assertEquals(3, stats.get("totalDevices"));
        assertEquals(2, stats.get("activeDevices"));

        @SuppressWarnings("unchecked")
        Map<String, Integer> devicesByType = (Map<String, Integer>) stats.get("devicesByType");
        assertEquals(1, devicesByType.get("Router"));
        assertEquals(1, devicesByType.get("Switch"));
        assertEquals(1, devicesByType.get("Server"));
    }
}