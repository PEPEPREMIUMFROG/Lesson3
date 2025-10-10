package org.example.dao;

import org.example.model.Reader;

import java.sql.SQLException;
import java.util.Optional;
import java.sql.Connection;

public interface ReaderDAO {
    Reader save(Reader reader);
    Reader update(Reader reader);
    Optional<Reader> findById(Connection conn, Long id) throws SQLException;

}
