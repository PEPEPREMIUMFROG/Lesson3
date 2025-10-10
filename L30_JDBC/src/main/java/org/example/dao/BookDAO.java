package org.example.dao;

import org.example.model.Book;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Book save(Book book);
    List<Book> findBooksByStatus(String status);
    List<Book> findBooksBorrowedAfterDateNotReturned(java.time.LocalDate date);
    Optional<Book> findById(Connection conn, Long id) throws SQLException;
    boolean isBookCurrentlyBorrowed(Connection conn, Long bookId) throws SQLException;
}
