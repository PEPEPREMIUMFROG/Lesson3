package org.example.dao;

import org.example.model.BorrowedBook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookDAO {

    private final Connection connection;

    public BorrowedBookDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BorrowedBook> findBorrowedBooksByReaderId(int readerId) {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String sql = """
            SELECT bb.*, b.title, b.author, b.published_year, b.genre 
            FROM borrowed_books bb 
            JOIN books b ON bb.book_id = b.book_id 
            WHERE bb.reader_id = ? AND bb.status = 'borrowed'
            ORDER BY bb.borrow_date DESC
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, readerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                borrowedBooks.add(mapResultSetToBorrowedBook(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding borrowed books for reader: " + readerId, e);
        }
        return borrowedBooks;
    }

    public List<BorrowedBook> findAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String sql = """
            SELECT bb.*, b.title, b.author, b.published_year, b.genre, r.name as reader_name
            FROM borrowed_books bb 
            JOIN books b ON bb.book_id = b.book_id 
            JOIN readers r ON bb.reader_id = r.reader_id
            ORDER BY bb.borrow_date DESC
            """;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                borrowedBooks.add(mapResultSetToBorrowedBook(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding all borrowed books", e);
        }
        return borrowedBooks;
    }

    public BorrowedBook borrowBook(BorrowedBook borrowedBook) {
        String sql = """
            INSERT INTO borrowed_books (book_id, reader_id, borrow_date, return_date, status) 
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, borrowedBook.getBookId());
            stmt.setInt(2, borrowedBook.getReaderId());
            stmt.setDate(3, Date.valueOf(borrowedBook.getBorrowDate()));
            if (borrowedBook.getReturnDate() != null) {
                stmt.setDate(4, Date.valueOf(borrowedBook.getReturnDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, borrowedBook.getStatus());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    borrowedBook.setBorrowId(generatedKeys.getInt(1));
                }
            }
            return borrowedBook;
        } catch (SQLException e) {
            throw new DataAccessException("Error borrowing book", e);
        }
    }

    public void returnBook(int borrowId) {
        String sql = "UPDATE borrowed_books SET status = 'returned', return_date = CURRENT_DATE WHERE borrow_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, borrowId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error returning book with borrow_id: " + borrowId, e);
        }
    }

    private BorrowedBook mapResultSetToBorrowedBook(ResultSet rs) throws SQLException {
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(rs.getInt("borrow_id"));
        borrowedBook.setBookId(rs.getInt("book_id"));
        borrowedBook.setReaderId(rs.getInt("reader_id"));
        borrowedBook.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            borrowedBook.setReturnDate(returnDate.toLocalDate());
        }
        borrowedBook.setStatus(rs.getString("status"));
        return borrowedBook;
    }
}