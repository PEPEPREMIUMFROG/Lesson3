package org.example.dao;

import org.example.connection.DatabaseConnectionService;
import org.example.model.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReaderDAOImpl implements ReaderDAO {
    @Override
    public Reader save(Reader reader) {
        String sql = "INSERT INTO lesson_29.readers (name, email, phone) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reader.getName());
            stmt.setString(2, reader.getEmail());
            stmt.setString(3, reader.getPhone());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) reader.setId(rs.getLong("id"));
            return reader;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save reader", e);
        }
    }

    @Override
    public Reader update(Reader reader) {
        String sql = "UPDATE lesson_29.readers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, reader.getName());
            stmt.setString(2, reader.getEmail());
            stmt.setString(3, reader.getPhone());
            stmt.setLong(4, reader.getId());
            stmt.executeUpdate();
            return reader;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update reader", e);
        }
    }

    @Override
    public Optional<Reader> findById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM lesson_29.readers WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reader r = new Reader();
                r.setId(rs.getLong("id"));
                r.setName(rs.getString("name"));
                r.setEmail(rs.getString("email"));
                r.setPhone(rs.getString("phone"));
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }
}

