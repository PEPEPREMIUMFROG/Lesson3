
package soap;

import org.example.model.Book;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;
import org.example.soap.LibrarySoapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibrarySoapServiceImplTest {

    @Mock
    private Connection connection;

    @Mock
    private LibraryService libraryService;

    private LibrarySoapServiceImpl soapService;

    @BeforeEach
    void setUp() {
        soapService = new LibrarySoapServiceImpl() {
            @Override
            protected Connection getConnection() throws SQLException {
                return connection;
            }

            @Override
            protected LibraryService createLibraryService(Connection conn) {
                return libraryService;
            }
        };
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        int bookId = 1;
        Book expectedBook = new Book("Test Book", "Test Author", 2023, "Fiction");
        expectedBook.setBookId(bookId);
        when(libraryService.getBookById(bookId)).thenReturn(Optional.of(expectedBook));
        Book result = soapService.getBookById(bookId);
        assertNotNull(result);
        assertEquals(bookId, result.getBookId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals(2023, (int) result.getPublishedYear());
        assertEquals("Fiction", result.getGenre());
        verify(libraryService).getBookById(bookId);
    }

    @Test
    void getBookById_WhenBookNotFound_ShouldThrowException() {
        int bookId = 999;
        when(libraryService.getBookById(bookId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> soapService.getBookById(bookId));
        assertTrue(exception.getMessage().contains("не найдена"));
        verify(libraryService).getBookById(bookId);
    }

    @Test
    void getBookById_WhenDatabaseError_ShouldThrowException() {
        int bookId = 1;
        when(libraryService.getBookById(bookId))
                .thenThrow(new RuntimeException("Database connection failed"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> soapService.getBookById(bookId));
        assertTrue(exception.getMessage().contains("Ошибка при получении книги"));
        verify(libraryService).getBookById(bookId);
    }

    @Test
    void addBook_WithValidData_ShouldReturnSavedBook() {
        String title = "New Book";
        String author = "New Author";
        int publishedYear = 2024;
        String genre = "Science Fiction";
        Book savedBook = new Book(title, author, publishedYear, genre);
        savedBook.setBookId(1);
        when(libraryService.addBook(any(Book.class))).thenReturn(savedBook);
        Book result = soapService.addBook(title, author, publishedYear, genre);
        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(publishedYear, (int) result.getPublishedYear());
        assertEquals(genre, result.getGenre());
        verify(libraryService).addBook(any(Book.class));
    }

    @Test
    void addBook_WithInvalidData_ShouldThrowException() {
        String title = "";
        String author = "Author";
        int publishedYear = 2024;
        String genre = "Genre";
        when(libraryService.addBook(any(Book.class)))
                .thenThrow(new LibraryServiceException("Book title cannot be empty"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> soapService.addBook(title, author, publishedYear, genre));
        assertTrue(exception.getMessage().contains("Ошибка при добавлении книги"));
        verify(libraryService).addBook(any(Book.class));
    }

    @Test
    void addBook_WithDatabaseError_ShouldThrowException() {
        String title = "Valid Title";
        String author = "Valid Author";
        int publishedYear = 2024;
        String genre = "Valid Genre";
        when(libraryService.addBook(any(Book.class)))
                .thenThrow(new RuntimeException("Database constraint violation"));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> soapService.addBook(title, author, publishedYear, genre));
        assertTrue(exception.getMessage().contains("Ошибка при добавлении книги"));
        verify(libraryService).addBook(any(Book.class));
    }
}