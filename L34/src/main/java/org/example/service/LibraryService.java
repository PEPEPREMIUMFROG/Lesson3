package org.example.service;

import org.example.dao.BookDAO;
import org.example.dao.BorrowedBookDAO;
import org.example.dao.ReaderDAO;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private final BookDAO bookDAO;
    private final ReaderDAO readerDAO;
    private final BorrowedBookDAO borrowedBookDAO;
    private final Connection connection;

    public LibraryService(Connection connection) {
        this.connection = connection;
        this.bookDAO = new BookDAO(connection);
        this.readerDAO = new ReaderDAO(connection);
        this.borrowedBookDAO = new BorrowedBookDAO(connection);
    }

    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    public Optional<Book> getBookById(int bookId) {
        return bookDAO.findById(bookId);
    }

    public Book addBook(Book book) {
        validateBook(book);
        return bookDAO.save(book);
    }

    public List<Reader> getAllReaders() {
        return readerDAO.findAll();
    }

    public Optional<Reader> getReaderById(int readerId) {
        return readerDAO.findById(readerId);
    }

    public Reader addReader(Reader reader) {
        validateReader(reader);
        return readerDAO.save(reader);
    }

    public List<BorrowedBook> getBorrowedBooksByReaderId(int readerId) {
        return borrowedBookDAO.findBorrowedBooksByReaderId(readerId);
    }

    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookDAO.findAllBorrowedBooks();
    }

    public BorrowedBook borrowBook(int bookId, int readerId) {
        Book book = bookDAO.findById(bookId).orElseThrow(() -> new LibraryServiceException("Book not found with id: " + bookId));
        Reader reader = readerDAO.findById(readerId).orElseThrow(() -> new LibraryServiceException("Reader not found with id: " + readerId));
        BorrowedBook borrowedBook = new BorrowedBook(bookId, readerId, LocalDate.now(), null, "borrowed");
        return borrowedBookDAO.borrowBook(borrowedBook);
    }

    public void returnBook(int borrowId) {
        borrowedBookDAO.returnBook(borrowId);
    }

    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new LibraryServiceException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new LibraryServiceException("Book author cannot be empty");
        }
        if (book.getPublishedYear() == null || book.getPublishedYear() <= 0) {
            throw new LibraryServiceException("Invalid publication year");
        }
    }

    private void validateReader(Reader reader) {
        if (reader.getName() == null || reader.getName().trim().isEmpty()) {
            throw new LibraryServiceException("Reader name cannot be empty");
        }
        if (reader.getEmail() == null || reader.getEmail().trim().isEmpty()) {
            throw new LibraryServiceException("Reader email cannot be empty");
        }
    }

}