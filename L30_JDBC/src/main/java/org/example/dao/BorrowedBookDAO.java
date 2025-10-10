package org.example.dao;

import org.example.model.BorrowedBook;
import org.example.model.OccupiedBook;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BorrowedBookDAO {
    List<OccupiedBook> findOccupiedBooks();
    BorrowedBook save(Connection conn, BorrowedBook book) throws SQLException;
    void markAsReturned(Connection conn, Long bookId) throws SQLException;
}
