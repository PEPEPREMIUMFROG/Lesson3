package org.example.service;

import org.example.dao.BookDAO;
import org.example.dao.BorrowedBookDAO;
import org.example.dao.ReaderDAO;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    private final BookDAO bookDAO;
    private final ReaderDAO readerDAO;
    private final BorrowedBookDAO borrowedBookDAO;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.readerDAO = new ReaderDAO();
        this.borrowedBookDAO = new BorrowedBookDAO();
    }

    public LibraryService(BookDAO bookDAO, ReaderDAO readerDAO, BorrowedBookDAO borrowedBookDAO) {
        this.bookDAO = bookDAO;
        this.readerDAO = readerDAO;
        this.borrowedBookDAO = borrowedBookDAO;
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

    public List<Book> findBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new LibraryServiceException("Title cannot be empty");
        }
        return bookDAO.findByTitle(title);
    }

    public List<Book> findBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new LibraryServiceException("Author cannot be empty");
        }
        return bookDAO.findByAuthor(author);
    }

    public List<Reader> getAllReaders() {
        return readerDAO.findAll();
    }

    public Optional<Reader> getReaderById(int readerId) {
        return readerDAO.findById(readerId);
    }

    public Reader addReader(Reader reader) {
        validateReader(reader);
        Optional<Reader> existingReader = readerDAO.findByEmail(reader.getEmail());
        if (existingReader.isPresent()) {
            throw new LibraryServiceException("Reader with email '" + reader.getEmail() + "' already exists");
        }

        return readerDAO.save(reader);
    }

    public Optional<Reader> findReaderByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new LibraryServiceException("Email cannot be empty");
        }
        return readerDAO.findByEmail(email);
    }

    public List<BorrowedBook> getBorrowedBooksByReaderId(int readerId) {
        return borrowedBookDAO.findBorrowedBooksByReaderId(readerId);
    }

    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookDAO.findAllBorrowedBooks();
    }

    public BorrowedBook borrowBook(int bookId, int readerId) {
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new LibraryServiceException("Book not found with id: " + bookId));
        Reader reader = readerDAO.findById(readerId)
                .orElseThrow(() -> new LibraryServiceException("Reader not found with id: " + readerId));
        List<BorrowedBook> currentBorrows = borrowedBookDAO.findBorrowedBooksByReaderId(readerId);
        boolean alreadyBorrowed = currentBorrows.stream()
                .anyMatch(bb -> bb.getBook().getBookId().equals(bookId) && "borrowed".equals(bb.getStatus()));
        if (alreadyBorrowed) {
            throw new LibraryServiceException("Book is already borrowed by this reader");
        }
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setReturnDate(null);
        borrowedBook.setStatus("borrowed");
        return borrowedBookDAO.borrowBook(borrowedBook);
    }

    public void returnBook(int borrowId) {
        borrowedBookDAO.returnBook(borrowId);
    }

    public List<BorrowedBook> getOverdueBooks() {
        return borrowedBookDAO.findOverdueBooks();
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
        if (book.getPublishedYear() > LocalDate.now().getYear()) {
            throw new LibraryServiceException("Publication year cannot be in the future");
        }
    }

    private void validateReader(Reader reader) {
        if (reader.getName() == null || reader.getName().trim().isEmpty()) {
            throw new LibraryServiceException("Reader name cannot be empty");
        }
        if (reader.getEmail() == null || reader.getEmail().trim().isEmpty()) {
            throw new LibraryServiceException("Reader email cannot be empty");
        }
        if (!reader.getEmail().contains("@")) {
            throw new LibraryServiceException("Invalid email format");
        }
    }

    public void shutdown() {
    }
}