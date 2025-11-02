package dao;

import org.example.dao.BookDAO;
import org.example.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAO(connection);
    }

    @Test
    void testFindById_Success() throws SQLException {
        int bookId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("title")).thenReturn("Test Book");
        when(resultSet.getString("author")).thenReturn("Test Author");
        when(resultSet.getInt("published_year")).thenReturn(2020);
        when(resultSet.getString("genre")).thenReturn("Fiction");
        Optional<Book> result = bookDAO.findById(bookId);
        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        assertEquals("Test Author", result.get().getAuthor());
        verify(preparedStatement).setInt(1, bookId);
    }

    @Test
    void testFindById_NotFound() throws SQLException {
        int bookId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        Optional<Book> result = bookDAO.findById(bookId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_Success() throws SQLException {when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("title")).thenReturn("Book 1", "Book 2");
        when(resultSet.getString("author")).thenReturn("Author 1", "Author 2");
        when(resultSet.getInt("published_year")).thenReturn(2020, 2021);
        when(resultSet.getString("genre")).thenReturn("Fiction", "Science");
        List<Book> result = bookDAO.findAll();
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
    }

    @Test
    void testSave_InsertNewBook() throws SQLException {
        Book book = new Book("New Book", "New Author", 2023, "Fantasy");
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        Book result = bookDAO.save(book);
        assertNotNull(result);
        assertEquals(1, result.getBookId());
        verify(preparedStatement).setString(1, "New Book");
        verify(preparedStatement).setString(2, "New Author");
        verify(preparedStatement).setInt(3, 2023);
        verify(preparedStatement).setString(4, "Fantasy");
        verify(preparedStatement).executeUpdate();
    }
}