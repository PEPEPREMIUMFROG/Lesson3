package org.example.service;

import org.example.dao.NetworkDao;
import org.example.ui.UiController;
import org.example.util.ValidationUtil;

import java.sql.SQLException;

public class NetworksService {
    private final UiController uiController;
    private final NetworkDao networkDao;

    public NetworksService(UiController uiController, NetworkDao networkDao) {
        this.uiController = uiController;
        this.networkDao = networkDao;
    }


    public void process() {
        while (true) {
            int userCode = uiController.getUserChoice();
            var action = UserChoice.valueOf(userCode);
            if (action.isPresent()) {
                var userChoice = action.get();
                if (userChoice == UserChoice.EXIT)
                    break;

                try {
                    processChoice(userChoice);
                } catch (Exception e) {
                    uiController.printError(e.getMessage());
                }

            } else {
                uiController.printError("Code was incorrect; try again");
            }
        }
    }

    public void processChoice(UserChoice userChoice) throws SQLException {
        switch (userChoice) {
            case UserChoice.ADD_NETWORK -> {
                var network = uiController.readNetwork();
                network = networkDao.save(network);
                uiController.print(network);
            }
            case ADD_DEVICE -> {
                var device = uiController.readDevice();

                var networks = networkDao.getAllNetworks();
                if (networks.isEmpty()) {
                    uiController.printError("No networks found; add network first");
                    return;
                }

                var network = uiController.selectOf(networks, "network");
                device.setNetworkId(network.getId());
                ValidationUtil.validateDevice(device);
                device = networkDao.save(device);
                uiController.print(device);
            }
            case ADD_CONNECTION -> {
                var allDevices = networkDao.getAllDevices();
                if (allDevices.size() < 2) {
                    uiController.printError("Not enough devices");
                    return;
                }

                var connection = uiController.readConnection();
                var deviceFrom = uiController.selectOf(allDevices, "device from");
                var remainedDevices = allDevices.stream()
                        .filter(device -> !device.equals(deviceFrom))
                        .toList();
                var deviceTo = uiController.selectOf(remainedDevices, "device to");

                connection.setDeviceFromId(deviceFrom.getId());
                connection.setDeviceToId(deviceTo.getId());
                connection = networkDao.save(connection);
                uiController.print(connection);
            }
            case EDIT_NETWORK -> {
                var networks = networkDao.getAllNetworks();
                var networkToEdit = uiController.selectOf(networks, "network to edit");
                networkToEdit = uiController.updateNetwork(networkToEdit);
                networkToEdit = networkDao.update(networkToEdit);
                uiController.print(networkToEdit);
            }
            case REMOVE_CONNECTION -> {
                var connections = networkDao.getAllConnections();
                var connectionToRemove = uiController.selectOf(connections, "connection to remove");
                networkDao.remove(connectionToRemove, "connections");
                uiController.printError("Connection " + connectionToRemove.getId() + " was removed");
            }
            case REMOVE_NETWORK -> {
                var networks = networkDao.getEmptyNetworks();

                if (networks.isEmpty()) {
                    uiController.printError("All networks have related devices. Remove them first");
                    return;
                }

                var networkToRemove = uiController.selectOf(networks, "network to remove");
                networkDao.remove(networkToRemove, "network");
                uiController.printError("Network " + networkToRemove.getId() + " was removed");
            }
            case SEARCH_NETWORKS -> {
                String name = uiController.readStringField("network name to search");
                var networks = networkDao.findNetworksByName(name);
                uiController.printSearchResults(networks);
            }
            case SEARCH_DEVICES -> {
                String ip = uiController.readOptionalField("IP address (press Enter to skip)");
                String type = uiController.readOptionalField("device type (press Enter to skip)");
                String status = uiController.readOptionalField("status (press Enter to skip)");

                var devices = networkDao.findDevicesByCriteria(ip, type, status);
                uiController.printSearchResults(devices);
            }
            case VIEW_NETWORK_STATISTICS -> {
                var networks = networkDao.getAllNetworks();
                var network = uiController.selectOf(networks, "network for statistics");
                var statistics = networkDao.getNetworkStatistics(network.getId());
                uiController.printStatistics(statistics);
            }
            case VIEW_NETWORK_DETAILS -> {
                var networks = networkDao.getAllNetworks();
                var network = uiController.selectOf(networks, "network to view details");
                var devices = networkDao.findDevicesByNetworkId(network.getId());
                var connections = networkDao.getConnectionsByNetworkId(network.getId());
                uiController.printNetworkDetails(network, devices, connections);
            }

            case GENERATE_REPORT -> {
                var networks = networkDao.getAllNetworks();
                uiController.generateReport(networks, networkDao);
            }
            case EDIT_DEVICE -> {
                var devices = networkDao.getAllDevices();
                var deviceToEdit = uiController.selectOf(devices, "device to edit");
                deviceToEdit = uiController.updateDevice(deviceToEdit);
                ValidationUtil.validateDevice(deviceToEdit);
                deviceToEdit = networkDao.update(deviceToEdit);
                uiController.print(deviceToEdit);
            }
            case REMOVE_DEVICE -> {
                var devices = networkDao.getAllDevices();
                var deviceToRemove = uiController.selectOf(devices, "device to remove");
                networkDao.removeDevice(deviceToRemove.getId());
                uiController.printError("Device " + deviceToRemove.getId() + " was removed");
            }
            default -> uiController.printError("Something went wrong");
        }
    }
}
