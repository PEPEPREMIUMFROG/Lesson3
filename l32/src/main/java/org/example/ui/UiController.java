package org.example.ui;

import org.example.dao.NetworkDao;
import org.example.model.Device;
import org.example.model.DeviceConnection;
import org.example.model.Model;
import org.example.model.Network;
import org.example.service.UserChoice;

import java.io.PrintStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UiController {
    private final Scanner scanner;
    private final PrintStream printStream;

    public UiController(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.printStream = printStream;
    }

    public int getUserChoice() {
        printStream.println("Enter: ");
        for (var action : UserChoice.values()) {
            System.out.println(action.getCode() + " " + action.getDescription());
        }

        int code = scanner.nextInt();
        scanner.nextLine();
        return code;
    }

    public void print(Network network) {
        printStream.println(network.toString());
    }

    public void print(DeviceConnection connection) {
        printStream.println(connection.toString());
    }

    public void print(Device device) {
        printStream.println(device.toString());
    }

    public Network readNetwork() {
        return updateNetwork(new Network());
    }

    public Network updateNetwork(Network network) {
        network.setName(readStringField("name"));
        network.setDescription(readStringField("description"));
        return network;
    }

    public DeviceConnection readConnection() {
        String status = readStringField("status");
        String type = readStringField("type");
        return new DeviceConnection(type, status);
    }


    public <T extends Model> T selectOf(List<T> models, String name) {
        while (true) {
            printStream.println("Enter id of " + name+":");
            models.forEach(printStream::println);
            var id = scanner.nextInt();
            scanner.nextLine();

            for(var model: models) {
                if(model.getId() == id)
                    return model;
            }

            printStream.println("Id was incorrect; try again");
        }
    }


    public Device readDevice() {
        String name = readStringField("name");
        String ipAddress = readStringField("ipAddress");
        String macAddress = readStringField("macAddress");
        String type = readStringField("type");
        String status = readStringField("status");
        return new Device(name, ipAddress, macAddress, type, status);
    }

    public String readStringField(String fieldName) {
        System.out.println("Enter " + fieldName + ":");
        return scanner.nextLine();
    }

    public void printError(String message) {
        printStream.println(message);
    }

    public String readOptionalField(String fieldName) {
        System.out.println("Enter " + fieldName + ":");
        String input = scanner.nextLine();
        return input.isEmpty() ? null : input;
    }

    public void printSearchResults(List<?> results) {
        if (results.isEmpty()) {
            printStream.println("No results found");
        } else {
            results.forEach(printStream::println);
        }
    }

    public void printStatistics(Map<String, Object> statistics) {
        statistics.forEach((key, value) ->
                printStream.println(key + ": " + value)
        );
    }

    public void printNetworkDetails(Network network, List<Device> devices, List<DeviceConnection> connections) {
        printStream.println("Network Details: ");
        printStream.println(network);
        printStream.println("Devices in Network: ");
        devices.forEach(printStream::println);
        printStream.println("Connections in Network: ");
        connections.forEach(printStream::println);
    }
    public void generateReport(List<Network> networks, NetworkDao networkDao) throws SQLException {
        printStream.println("Network report: ");
        printStream.println("Generated: " + LocalDateTime.now());
        printStream.println("Total networks: " + networks.size());

        for (Network network : networks) {
            printStream.println("\n--- Network: " + network.getName() + " ---");
            var devices = networkDao.findDevicesByNetworkId(network.getId());
            var stats = networkDao.getNetworkStatistics(network.getId());

            printStream.println("Devices: " + stats.get("totalDevices"));
            printStream.println("Active devices: " + stats.get("activeDevices"));

            @SuppressWarnings("unchecked")
            Map<String, Integer> devicesByType = (Map<String, Integer>) stats.get("devicesByType");
            if (devicesByType != null) {
                printStream.println("Devices by type:");
                devicesByType.forEach((type, count) -> printStream.println("  " + type + ": " + count));
            }
        }
    }

    public Device updateDevice(Device device) {
        device.setName(readOptionalFieldWithDefault("name", device.getName()));
        device.setIpAddress(readOptionalFieldWithDefault("ip address", device.getIpAddress()));
        device.setMacAddress(readOptionalFieldWithDefault("mac address", device.getMacAddress()));
        device.setType(readOptionalFieldWithDefault("type", device.getType()));
        device.setStatus(readOptionalFieldWithDefault("status", device.getStatus()));
        return device;
    }

    private String readOptionalFieldWithDefault(String fieldName, String currentValue) {
        System.out.println("Enter " + fieldName + " (current: " + currentValue + ", press Enter to keep):");
        String input = scanner.nextLine();
        return input.isEmpty() ? currentValue : input;
    }


}
