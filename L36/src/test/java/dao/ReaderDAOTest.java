package dao;

import org.example.dao.ReaderDAO;
import org.example.model.Reader;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private ReaderDAO readerDAO;

    @BeforeEach
    void setUp() {
        readerDAO = new ReaderDAO(connection);
    }

    @Test
    void testFindById_Success() throws SQLException {
        int readerId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("reader_id")).thenReturn(readerId);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");
        Optional<Reader> result = readerDAO.findById(readerId);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("john@example.com", result.get().getEmail());
        verify(preparedStatement).setInt(1, readerId);
    }

    @Test
    void testFindAll_Success() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("reader_id")).thenReturn(1, 2);
        when(resultSet.getString("name")).thenReturn("Reader 1", "Reader 2");
        when(resultSet.getString("email")).thenReturn("email1@test.com", "email2@test.com");
        when(resultSet.getString("phone")).thenReturn("111", "222");
        List<Reader> result = readerDAO.findAll();
        assertEquals(2, result.size());
        assertEquals("Reader 1", result.get(0).getName());
        assertEquals("Reader 2", result.get(1).getName());
    }

    @Test
    void testSave_InsertNewReader() throws SQLException {
        Reader reader = new Reader("New Reader", "new@example.com", "555555555");
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        Reader result = readerDAO.save(reader);
        assertNotNull(result);
        assertEquals(1, result.getReaderId());
        verify(preparedStatement).setString(1, "New Reader");
        verify(preparedStatement).setString(2, "new@example.com");
        verify(preparedStatement).setString(3, "555555555");
        verify(preparedStatement).executeUpdate();
    }
}