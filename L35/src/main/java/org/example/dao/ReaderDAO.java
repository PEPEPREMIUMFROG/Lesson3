package org.example.dao;

import org.example.model.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDAO {

    private final Connection connection;

    public ReaderDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Reader> findById(int readerId) {
        String sql = "SELECT * FROM readers WHERE reader_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToReader(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding reader by id: " + readerId, e);
        }
        return Optional.empty();
    }

    public List<Reader> findAll() {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT * FROM readers ORDER BY name";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                readers.add(mapResultSetToReader(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all readers", e);
        }
        return readers;
    }

    public Reader save(Reader reader) {
        if (reader.getReaderId() == null) {
            return insert(reader);
        } else {
            return update(reader);
        }
    }

    private Reader insert(Reader reader) {
        String sql = "INSERT INTO readers (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reader.getName());
            stmt.setString(2, reader.getEmail());
            stmt.setString(3, reader.getPhone());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reader.setReaderId(generatedKeys.getInt(1));
                }
            }
            return reader;
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting reader", e);
        }
    }

    private Reader update(Reader reader) {
        String sql = "UPDATE readers SET name = ?, email = ?, phone = ? WHERE reader_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reader.getName());
            stmt.setString(2, reader.getEmail());
            stmt.setString(3, reader.getPhone());
            stmt.setInt(4, reader.getReaderId());
            stmt.executeUpdate();
            return reader;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating reader", e);
        }
    }

    private Reader mapResultSetToReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader(rs.getString("name"), rs.getString("email"), rs.getString("phone"));
        reader.setReaderId(rs.getInt("reader_id"));
        return reader;
    }
}