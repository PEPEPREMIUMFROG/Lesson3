package service;

import org.example.dao.BookDAO;
import org.example.dao.BorrowedBookDAO;
import org.example.dao.ReaderDAO;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private ReaderDAO readerDAO;

    @Mock
    private BorrowedBookDAO borrowedBookDAO;

    private LibraryService libraryService;

    @BeforeEach
    void setUp() throws Exception {
        libraryService = new LibraryService(connection);
        var bookDAOField = LibraryService.class.getDeclaredField("bookDAO");
        var readerDAOField = LibraryService.class.getDeclaredField("readerDAO");
        var borrowedBookDAOField = LibraryService.class.getDeclaredField("borrowedBookDAO");
        bookDAOField.setAccessible(true);
        readerDAOField.setAccessible(true);
        borrowedBookDAOField.setAccessible(true);
        bookDAOField.set(libraryService, bookDAO);
        readerDAOField.set(libraryService, readerDAO);
        borrowedBookDAOField.set(libraryService, borrowedBookDAO);
    }

    @Test
    void testAddBook_ValidBook() {
        Book book = new Book("Valid Book", "Valid Author", 2023, "Fantasy");
        when(bookDAO.save(any(Book.class))).thenReturn(book);
        Book result = libraryService.addBook(book);
        assertNotNull(result);
        assertEquals("Valid Book", result.getTitle());
    }

    @Test
    void testAddBook_InvalidTitle() {
        Book book = new Book("", "Valid Author", 2023, "Fantasy");
        LibraryServiceException exception = assertThrows(LibraryServiceException.class,
                () -> libraryService.addBook(book));
        assertTrue(exception.getMessage().contains("Book title cannot be empty"));
    }

    @Test
    void testBorrowBook_Success() {
        Book book = new Book("Test Book", "Test Author", 2020, "Fiction");
        book.setBookId(1);
        Reader reader = new Reader("Test Reader", "test@example.com", "123456789");
        reader.setReaderId(1);
        BorrowedBook borrowedBook = new BorrowedBook(1, 1, LocalDate.now(), null, "borrowed");
        borrowedBook.setBorrowId(1);
        when(bookDAO.findById(1)).thenReturn(Optional.of(book));
        when(readerDAO.findById(1)).thenReturn(Optional.of(reader));
        when(borrowedBookDAO.borrowBook(any(BorrowedBook.class))).thenReturn(borrowedBook);
        BorrowedBook result = libraryService.borrowBook(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getBorrowId());
        assertEquals("borrowed", result.getStatus());
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookDAO.findById(1)).thenReturn(Optional.empty());
        LibraryServiceException exception = assertThrows(LibraryServiceException.class,
                () -> libraryService.borrowBook(1, 1));
        assertTrue(exception.getMessage().contains("Book not found with id: 1"));
    }

    @Test
    void testReturnBook() {
        int borrowId = 1;
        libraryService.returnBook(borrowId);
        verify(borrowedBookDAO).returnBook(borrowId);
    }
}