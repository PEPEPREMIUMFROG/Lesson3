package dao;

import org.example.dao.BorrowedBookDAO;
import org.example.model.BorrowedBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowedBookDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    private BorrowedBookDAO borrowedBookDAO;

    @BeforeEach
    void setUp() {
        borrowedBookDAO = new BorrowedBookDAO(connection);
    }

    @Test
    void testFindBorrowedBooksByReaderId_Success() throws SQLException {
        int readerId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("borrow_id")).thenReturn(1);
        when(resultSet.getInt("book_id")).thenReturn(1);
        when(resultSet.getInt("reader_id")).thenReturn(readerId);
        when(resultSet.getDate("borrow_date")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("return_date")).thenReturn(null);
        when(resultSet.getString("status")).thenReturn("borrowed");
        List<BorrowedBook> result = borrowedBookDAO.findBorrowedBooksByReaderId(readerId);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBorrowId());
        assertEquals("borrowed", result.get(0).getStatus());
        verify(preparedStatement).setInt(1, readerId);
    }

    @Test
    void testBorrowBook_Success() throws SQLException {
        BorrowedBook borrowedBook = new BorrowedBook(1, 1, LocalDate.now(), null, "borrowed");
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        BorrowedBook result = borrowedBookDAO.borrowBook(borrowedBook);
        assertNotNull(result);
        assertEquals(1, result.getBorrowId());
        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).setDate(3, Date.valueOf(LocalDate.now()));
        verify(preparedStatement).setNull(4, Types.DATE);
        verify(preparedStatement).setString(5, "borrowed");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testReturnBook_Success() throws SQLException {
        int borrowId = 1;
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        borrowedBookDAO.returnBook(borrowId);
        verify(preparedStatement).setInt(1, borrowId);
        verify(preparedStatement).executeUpdate();
    }
}