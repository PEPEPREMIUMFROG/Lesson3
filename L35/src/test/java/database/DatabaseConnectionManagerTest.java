package database;

import org.example.database.DatabaseConnectionManager;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseConnectionManagerSimpleTest {

    @Test
    void testCloseConnection_Success() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        doNothing().when(mockConnection).close();
        DatabaseConnectionManager.closeConnection(mockConnection);
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testCloseConnection_WithException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        doThrow(new SQLException("Connection closed")).when(mockConnection).close();
        assertDoesNotThrow(() -> DatabaseConnectionManager.closeConnection(mockConnection));
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testCloseConnection_NullConnection() {
        assertDoesNotThrow(() -> DatabaseConnectionManager.closeConnection(null));
    }

    @Test
    void testStaticMethodsExist() {
        assertDoesNotThrow(() -> {
            Class<DatabaseConnectionManager> clazz = DatabaseConnectionManager.class;
            clazz.getMethod("getConnection");
            clazz.getMethod("closeConnection", Connection.class);
        });
    }
}