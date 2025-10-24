package org.example.dao;

import org.example.config.AppConfig;
import org.example.model.Device;
import org.example.model.DeviceConnection;
import org.example.model.Model;
import org.example.model.Network;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkDao {

    private final AppConfig config;

    public NetworkDao() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.config = new AppConfig();
    }

    public NetworkDao(AppConfig config) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.config = config;
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getDatabaseUrl(),
                config.getDatabaseUsername(),
                config.getDatabasePassword()
        );
    }


    public Network save(Network network) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into " + schema + ".network (name,description) values(?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, network.getName());
                preparedStatement.setString(2, network.getDescription());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                network.setId(generatedKeys.getLong("id"));
                network.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return network;
            }
        }
    }

    public Network update(Network network) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "update " + schema + ".network set name=?,description=? where id=? returning id, name, description, created_at")) {
                preparedStatement.setString(1, network.getName());
                preparedStatement.setString(2, network.getDescription());
                preparedStatement.setLong(3, network.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();

                return toModel(resultSet, network);
            }
        }
    }

    public Device save(Device device) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into " + schema + ".device (name,ip_address,mac_address,type,status, network_id) values(?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, device.getName());
                preparedStatement.setString(2, device.getIpAddress());
                preparedStatement.setString(3, device.getMacAddress());
                preparedStatement.setString(4, device.getType());
                preparedStatement.setString(5, device.getStatus());
                preparedStatement.setLong(6, device.getNetworkId());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                device.setId(generatedKeys.getLong("id"));
                device.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return device;
            }
        }
    }

    public DeviceConnection save(DeviceConnection deviceConnection) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "insert into " + schema + ".connections (device_from_id, device_to_id,connection_type,status) values(?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, deviceConnection.getDeviceFromId());
                preparedStatement.setLong(2, deviceConnection.getDeviceToId());
                preparedStatement.setString(3, deviceConnection.getType());
                preparedStatement.setString(4, deviceConnection.getStatus());

                preparedStatement.execute();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                deviceConnection.setId(generatedKeys.getLong("id"));
                deviceConnection.setCreatedAt(generatedKeys.getTimestamp("created_at"));
                return deviceConnection;
            }
        }
    }

    public List<Network> getAllNetworks() throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from " + schema + ".network");

                var networks = new ArrayList<Network>();
                while (result.next()) {
                    networks.add(toModel(result));
                }

                return networks;
            }
        }
    }

    public List<Network> getEmptyNetworks() throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery(
                        "select ns.* from " + schema + ".network ns " +
                                "left join " + schema + ".device devices on ns.id = devices.network_id " +
                                "where devices.id IS NULL");

                var networks = new ArrayList<Network>();
                while (result.next()) {
                    networks.add(toModel(result));
                }

                return networks;
            }
        }
    }

    public List<Device> getAllDevices() throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from " + schema + ".device");

                var devices = new ArrayList<Device>();
                while (result.next()) {
                    devices.add(toDeviceModel(result));
                }

                return devices;
            }
        }
    }

    public void remove(Model model, String table) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "delete from " + schema + "." + table + " where id =?")) {
                preparedStatement.setLong(1, model.getId());
                preparedStatement.execute();
            }
        }
    }

    public List<DeviceConnection> getAllConnections() throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (var statement = connection.createStatement()) {
                var result = statement.executeQuery("select * from " + schema + ".connections");

                var connections = new ArrayList<DeviceConnection>();
                while (result.next()) {
                    connections.add(toDeviceConnectionModel(result));
                }

                return connections;
            }
        }
    }

    private Network toModel(ResultSet resultSet) throws SQLException {
        return toModel(resultSet, new Network());
    }

    private Network toModel(ResultSet resultSet, Network network) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var description = resultSet.getString("description");
        var createdAt = resultSet.getTimestamp("created_at");
        network.setId(id);
        network.setName(name);
        network.setDescription(description);
        network.setCreatedAt(createdAt);
        return network;
    }

    private Device toDeviceModel(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var ip = resultSet.getString("ip_address");
        var mac = resultSet.getString("mac_address");
        var type = resultSet.getString("type");
        var status = resultSet.getString("status");
        var networkId = resultSet.getLong("network_id");
        var createdAt = resultSet.getTimestamp("created_at");
        return new Device(id, networkId, name, ip, mac, type, status, createdAt);
    }

    private DeviceConnection toDeviceConnectionModel(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var type = resultSet.getString("connection_type");
        var status = resultSet.getString("status");
        var deviceFromId = resultSet.getLong("device_from_id");
        var deviceToId = resultSet.getLong("device_to_id");
        var createdAt = resultSet.getTimestamp("created_at");
        return new DeviceConnection(id, deviceFromId, deviceToId, type, status, createdAt);
    }

    public List<Network> findNetworksByName(String name) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM " + schema + ".network WHERE name ILIKE ?")) {
                ps.setString(1, "%" + name + "%");
                ResultSet result = ps.executeQuery();

                var networks = new ArrayList<Network>();
                while (result.next()) {
                    networks.add(toModel(result));
                }
                return networks;
            }
        }
    }

    public List<Device> findDevicesByNetworkId(Long networkId) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM " + schema + ".device WHERE network_id = ?")) {
                ps.setLong(1, networkId);
                ResultSet result = ps.executeQuery();

                var devices = new ArrayList<Device>();
                while (result.next()) {
                    devices.add(toDeviceModel(result));
                }
                return devices;
            }
        }
    }

    public List<Device> findDevicesByCriteria(String ip, String type, String status) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            StringBuilder sql = new StringBuilder("SELECT * FROM " + schema + ".device WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (ip != null && !ip.isEmpty()) {
                sql.append(" AND ip_address ILIKE ?");
                params.add("%" + ip + "%");
            }
            if (type != null && !type.isEmpty()) {
                sql.append(" AND type = ?");
                params.add(type);
            }
            if (status != null && !status.isEmpty()) {
                sql.append(" AND status = ?");
                params.add(status);
            }

            try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }

                ResultSet result = ps.executeQuery();
                var devices = new ArrayList<Device>();
                while (result.next()) {
                    devices.add(toDeviceModel(result));
                }
                return devices;
            }
        }
    }

    public Map<String, Object> getNetworkStatistics(Long networkId) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            Map<String, Object> stats = new HashMap<>();

            String totalSql = "SELECT COUNT(*) as total FROM " + schema + ".device WHERE network_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(totalSql)) {
                ps.setLong(1, networkId);
                ResultSet result = ps.executeQuery();
                result.next();
                stats.put("totalDevices", result.getInt("total"));
            }

            String activeSql = "SELECT COUNT(*) as active FROM " + schema + ".device WHERE network_id = ? AND status = 'active'";
            try (PreparedStatement ps = connection.prepareStatement(activeSql)) {
                ps.setLong(1, networkId);
                ResultSet result = ps.executeQuery();
                result.next();
                stats.put("activeDevices", result.getInt("active"));
            }

            String typeSql = "SELECT type, COUNT(*) as count FROM " + schema + ".device WHERE network_id = ? GROUP BY type";
            try (PreparedStatement ps = connection.prepareStatement(typeSql)) {
                ps.setLong(1, networkId);
                ResultSet result = ps.executeQuery();
                Map<String, Integer> devicesByType = new HashMap<>();
                while (result.next()) {
                    devicesByType.put(result.getString("type"), result.getInt("count"));
                }
                stats.put("devicesByType", devicesByType);
            }

            return stats;
        }
    }

    public Device update(Device device) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "update " + schema + ".device set name=?, ip_address=?, mac_address=?, type=?, status=? where id=? returning *")) {
                preparedStatement.setString(1, device.getName());
                preparedStatement.setString(2, device.getIpAddress());
                preparedStatement.setString(3, device.getMacAddress());
                preparedStatement.setString(4, device.getType());
                preparedStatement.setString(5, device.getStatus());
                preparedStatement.setLong(6, device.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return toDeviceModel(resultSet);
            }
        }
    }

    public void removeDevice(long deviceId) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();

            try (PreparedStatement ps = connection.prepareStatement(
                    "delete from " + schema + ".connections where device_from_id = ? or device_to_id = ?")) {
                ps.setLong(1, deviceId);
                ps.setLong(2, deviceId);
                ps.execute();
            }

            try (PreparedStatement ps = connection.prepareStatement(
                    "delete from " + schema + ".device where id = ?")) {
                ps.setLong(1, deviceId);
                ps.execute();
            }
        }
    }

    public List<DeviceConnection> getConnectionsByNetworkId(Long networkId) throws SQLException {
        try (Connection connection = openConnection()) {
            String schema = config.getDatabaseSchema();
            String sql = "SELECT c.* FROM " + schema + ".connections c " +
                    "JOIN " + schema + ".device d1 ON c.device_from_id = d1.id " +
                    "JOIN " + schema + ".device d2 ON c.device_to_id = d2.id " +
                    "WHERE d1.network_id = ? OR d2.network_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, networkId);
                ps.setLong(2, networkId);

                ResultSet result = ps.executeQuery();
                var connections = new ArrayList<DeviceConnection>();
                while (result.next()) {
                    connections.add(toDeviceConnectionModel(result));
                }
                return connections;
            }
        }
    }
}
