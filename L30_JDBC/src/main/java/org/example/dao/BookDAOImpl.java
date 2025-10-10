package org.example.dao;

import org.example.connection.DatabaseConnectionService;
import org.example.model.Book;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO lesson_29.books (title, author, published_year, genre) " + "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionService.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getPublishedYear());
            stmt.setString(4, book.getGenre());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                book.setId(rs.getLong("id"));
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save book", e);
        }
    }


    @Override
    public List<Book> findBooksByStatus(String status) {
        if ("borrowed".equalsIgnoreCase(status)) {
            String sql = """
                    SELECT DISTINCT b.* 
                    FROM lesson_29.books b
                    JOIN lesson_29.borrowed_books bb ON b.id = bb.book_id
                    WHERE bb.status = 'borrowed' AND bb.return_date IS NULL
                    """;
            return executeBookQuery(sql);
        } else if ("returned".equalsIgnoreCase(status)) {
            String sql = """
                    SELECT DISTINCT b.* 
                    FROM lesson_29.books b
                    JOIN lesson_29.borrowed_books bb ON b.id = bb.book_id
                    WHERE bb.status = 'returned'
                    """;
            return executeBookQuery(sql);
        } else {
            throw new IllegalArgumentException("Status must be 'borrowed' or 'returned'");
        }
    }


    @Override
    public List<Book> findBooksBorrowedAfterDateNotReturned(LocalDate date) {
        String sql = """
                SELECT DISTINCT b.* FROM lesson_29.books b
                JOIN lesson_29.borrowed_books bb ON b.id = bb.book_id
                WHERE bb.borrow_date > ? AND bb.return_date IS NULL
                """;
        try (Connection conn = DatabaseConnectionService.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find books borrowed after date", e);
        }
    }

    @Override
    public Optional<Book> findById(Connection conn, Long id) throws SQLException {
        String sql = "SELECT * FROM lesson_29.books WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBook(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isBookCurrentlyBorrowed(Connection conn, Long bookId) throws SQLException {
        String sql = """
                SELECT 1 FROM lesson_29.borrowed_books 
                WHERE book_id = ? AND status = 'borrowed' AND return_date IS NULL
                """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublishedYear(rs.getInt("published_year"));
        book.setGenre(rs.getString("genre"));
        return book;
    }

    private List<Book> executeBookQuery(String sql) {
        try (Connection conn = DatabaseConnectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException("Query execution failed", e);
        }
    }
}
