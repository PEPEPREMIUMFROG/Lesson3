package org.example;

import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.Network;
import org.example.service.NetworksService;
import org.example.ui.UiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NetworkServiceTest {

    @Mock
    private UiController uiController;

    @Mock
    private NetworkDao networkDao;

    private NetworksService networksService;

    @BeforeEach
    void setUp() {
        networksService = new NetworksService(uiController, networkDao);
    }

    @Test
    void testAddDeviceWhenNoNetworks() throws SQLException {
        when(networkDao.getAllNetworks()).thenReturn(Collections.emptyList());
        when(uiController.readDevice()).thenReturn(new Device("Test", "192.168.1.1", "00:00:00:00:00:00", "Router", "active"));
        networksService.processChoice(org.example.service.UserChoice.ADD_DEVICE);
        verify(uiController).printError("No networks found; add network first");
        verify(networkDao, never()).save(any(Device.class));
    }

    @Test
    void testSearchNetworks() throws SQLException {
        String searchName = "test";
        Network network = new Network("test network", "description");
        when(uiController.readStringField("network name to search")).thenReturn(searchName);
        when(networkDao.findNetworksByName(searchName)).thenReturn(Arrays.asList(network));
        networksService.processChoice(org.example.service.UserChoice.SEARCH_NETWORKS);
        verify(networkDao).findNetworksByName(searchName);
        verify(uiController).printSearchResults(any());
    }
}