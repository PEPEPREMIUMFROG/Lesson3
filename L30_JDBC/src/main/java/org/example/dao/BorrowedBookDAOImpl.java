package org.example.dao;

import org.example.connection.DatabaseConnectionService;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.OccupiedBook;
import org.example.model.Reader;
import org.example.model.BookStatus;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowedBookDAOImpl implements BorrowedBookDAO{

    @Override
    public List<OccupiedBook> findOccupiedBooks() {
        String sql = """
            SELECT b.id as book_id, b.title, b.author, b.published_year, b.genre,
                   r.id as reader_id, r.name, r.email, r.phone,
                   bb.borrow_date
            FROM lesson_29.borrowed_books bb
            JOIN lesson_29.books b ON bb.book_id = b.id
            JOIN lesson_29.readers r ON bb.reader_id = r.id
            WHERE bb.status = 'borrowed' AND bb.return_date IS NULL
            """;
        try (Connection conn = DatabaseConnectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<OccupiedBook> list = new ArrayList<>();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublishedYear(rs.getInt("published_year"));
                book.setGenre(rs.getString("genre"));

                Reader reader = new Reader();
                reader.setId(rs.getLong("reader_id"));
                reader.setName(rs.getString("name"));
                reader.setEmail(rs.getString("email"));
                reader.setPhone(rs.getString("phone"));
                LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                list.add(new OccupiedBook(book, reader, borrowDate));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch occupied books", e);
        }
    }


    @Override
    public BorrowedBook save(Connection conn, BorrowedBook bbook) throws SQLException {
        String sql = """
            INSERT INTO lesson_29.borrowed_books 
            (book_id, reader_id, borrow_date, return_date, status) 
            VALUES (?, ?, ?, ?, ?) RETURNING id
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bbook.getBookId());
            stmt.setLong(2, bbook.getReaderId());
            stmt.setDate(3, Date.valueOf(bbook.getBorrowDate()));
            stmt.setDate(4, bbook.getReturnDate() == null ? null : Date.valueOf(bbook.getReturnDate()));
            stmt.setString(5, bbook.getStatus().getDbVal());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bbook.setId(rs.getLong("id"));
            }
            return bbook;
        }
    }

    @Override
    public void markAsReturned(Connection conn, Long bookId) throws SQLException {
        String sql = """
            UPDATE lesson_29.borrowed_books 
            SET return_date = CURRENT_DATE, status = 'returned'
            WHERE book_id = ? AND status = 'borrowed' AND return_date IS NULL
            """;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookId);
            stmt.executeUpdate();
        }
    }

    private BorrowedBook mapRowToBorrowedBook(ResultSet rs) throws SQLException {
        BorrowedBook bb = new BorrowedBook();
        bb.setId(rs.getLong("id"));
        bb.setBookId(rs.getLong("book_id"));
        bb.setReaderId(rs.getLong("reader_id"));
        bb.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
        Date returnDate = rs.getDate("return_date");
        bb.setReturnDate(returnDate == null ? null : returnDate.toLocalDate());
        bb.setStatus(BookStatus.fromDbVal(rs.getString("status")));
        return bb;
    }
}
