package org.example.service;

import org.example.connection.DatabaseConnectionService;
import org.example.dao.BookDAO;
import org.example.dao.BorrowedBookDAO;
import org.example.dao.ReaderDAO;
import org.example.model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {

    private final BookDAO bookDAO;
    private final ReaderDAO readerDAO;
    private final BorrowedBookDAO borrowedBookDAO;

    public LibraryServiceImpl(BookDAO bookDAO, ReaderDAO readerDAO, BorrowedBookDAO borrowedBookDAO) {
        this.bookDAO = bookDAO;
        this.readerDAO = readerDAO;
        this.borrowedBookDAO = borrowedBookDAO;
    }

    @Override
    public Book addBook(Book book) {
        return bookDAO.save(book);
    }

    @Override
    public Reader addReader(Reader reader) {
        return readerDAO.save(reader);
    }

    @Override
    public Book borrowBook(Long bookId, Long readerId) {
        try (Connection conn = DatabaseConnectionService.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Book book = bookDAO.findById(conn, bookId)
                        .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));
                Reader reader = readerDAO.findById(conn, readerId)
                        .orElseThrow(() -> new IllegalArgumentException("Reader not found with id: " + readerId));
                if (bookDAO.isBookCurrentlyBorrowed(conn, bookId)) {
                    throw new IllegalStateException("Book is already borrowed");
                }
                BorrowedBook borrowedBook = new BorrowedBook();
                borrowedBook.setBookId(bookId);
                borrowedBook.setReaderId(readerId);
                borrowedBook.setBorrowDate(LocalDate.now());
                borrowedBook.setStatus(BookStatus.BORROWED);
                borrowedBookDAO.save(conn, borrowedBook);
                conn.commit();
                return book;

            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw new RuntimeException("Transaction failed in borrowBook", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to acquire DB connection", e);
        }
    }

    @Override
    public Book returnBook(Long bookId) {
        try (Connection conn = DatabaseConnectionService.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (!bookDAO.isBookCurrentlyBorrowed(conn, bookId)) {
                    throw new IllegalStateException("Book is not borrowed");
                }
                borrowedBookDAO.markAsReturned(conn, bookId);
                Book book = bookDAO.findById(conn, bookId)
                        .orElseThrow(() -> new RuntimeException("Book not found after return"));
                conn.commit();
                return book;
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw new RuntimeException("Transaction failed in returnBook", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to acquire DB connection", e);
        }
    }


    @Override
    public List<OccupiedBook> getOccupiedBooks() {
        return borrowedBookDAO.findOccupiedBooks();
    }

    @Override
    public Reader updateReader(Reader reader) {
        return readerDAO.update(reader);
    }


    @Override
    public List<Book> findBooksBorrowedAfterDateNotReturned(LocalDate date) {
        return bookDAO.findBooksBorrowedAfterDateNotReturned(date);
    }

    @Override
    public List<Book> findBooksByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        return bookDAO.findBooksByStatus(status.trim().toLowerCase());
    }
}

