package org.example.soap;

import jakarta.jws.WebService;
import org.example.database.DatabaseConnectionManager;
import org.example.model.Book;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@WebService(
        serviceName = "LibrarySoapService",
        portName = "LibrarySoapPort",
        targetNamespace = "http://soap.example.org/",
        endpointInterface = "org.example.soap.LibrarySoapService"
)
public class LibrarySoapServiceImpl implements LibrarySoapService {

    protected Connection getConnection() throws SQLException {
        Connection connection = DatabaseConnectionManager.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }


    protected LibraryService createLibraryService(Connection conn) {
        return new LibraryService(conn);
    }

    @Override
    public Book getBookById(int bookId) {
        Connection connection = null;
        try {
            connection = getConnection();
            LibraryService libraryService = createLibraryService(connection);
            Optional<Book> book = libraryService.getBookById(bookId);

            if (book.isPresent()) {
                connection.commit();
                return book.get();
            } else {
                throw new LibraryServiceException("Книга с ID " + bookId + " не найдена");
            }
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при получении книги: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Book addBook(String title, String author, int publishedYear, String genre) {
        Connection connection = null;
        try {
            connection = getConnection();
            LibraryService libraryService = createLibraryService(connection);

            Book book = new Book(title, author, publishedYear, genre);
            Book savedBook = libraryService.addBook(book);

            connection.commit();
            return savedBook;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Ошибка при добавлении книги: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

}