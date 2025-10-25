package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.database.DatabaseConnectionManager;
import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;
import org.example.service.LibraryService;
import org.example.service.LibraryServiceException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/library")
public class LibraryServlet extends HttpServlet {

    private LibraryService libraryService;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DatabaseConnectionManager.getConnection();
            connection.setAutoCommit(false);
            libraryService = new LibraryService(connection);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            StringBuilder html = new StringBuilder();
            html.append(generateHeader());
            html.append(generateMessageBlocks(req));
            if (action == null) {
                html.append(generateDashboard());
            } else {
                html.append(generateActionContent(action, req));
            }
            html.append(generateFooter());
            out.print(html.toString());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при обработке запроса: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            resp.sendRedirect("library?error=" + URLEncoder.encode("Не указано действие", "UTF-8"));
            return;
        }
        String redirectUrl = "library?";
        try {
            switch (action) {
                case "addBook":
                    addBook(req);
                    redirectUrl += "message=" + URLEncoder.encode("Книга успешно добавлена", "UTF-8");
                    break;
                case "addReader":
                    addReader(req);
                    redirectUrl += "message=" + URLEncoder.encode("Читатель успешно добавлен", "UTF-8");
                    break;
                case "borrowBook":
                    borrowBook(req);
                    redirectUrl += "message=" + URLEncoder.encode("Книга успешно выдана", "UTF-8");
                    break;
                case "returnBook":
                    returnBook(req);
                    redirectUrl += "message=" + URLEncoder.encode("Книга успешно возвращена", "UTF-8");
                    break;
                default:
                    redirectUrl += "error=" + URLEncoder.encode("Неизвестное действие: " + action, "UTF-8");
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex.getMessage());
            }
            redirectUrl += "error=" + URLEncoder.encode(e.getMessage(), "UTF-8").replace("+", "%20");
        }

        resp.sendRedirect(redirectUrl);
    }

    private String generateHeader() {
        return """
                <!DOCTYPE html>
                <html lang='ru'>
                <head>
                    <meta charset='UTF-8'>
                    <title>Библиотечная система</title>
                    <style>
                        body { 
                            font-family: Arial, sans-serif; 
                            margin: 20px; 
                            line-height: 1.4;
                        }
                        h1 { 
                            color: #2c3e50; 
                            border-bottom: 2px solid #3498db;
                            padding-bottom: 10px;
                        }
                        h2 { 
                            color: #2c3e50; 
                            margin-top: 20px;
                        }
                        h3 { 
                            color: #34495e; 
                            margin: 15px 0 10px 0;
                        }
                        .nav { 
                            margin: 20px 0; 
                            padding: 10px 0;
                            border-bottom: 1px solid #ddd;
                        }
                        .nav a { 
                            display: inline-block;
                            margin-right: 15px; 
                            padding: 8px 15px;
                            background: #3498db; 
                            color: white; 
                            text-decoration: none; 
                            border-radius: 3px;
                        }
                        .nav a:hover { 
                            background: #2980b9; 
                        }
                        .card { 
                            background: white; 
                            padding: 15px; 
                            margin: 15px 0; 
                            border: 1px solid #ddd;
                            border-radius: 4px;
                        }
                        .book-item, .reader-item { 
                            margin: 10px 0; 
                            padding: 10px;
                            border-left: 3px solid #3498db;
                            background: #f8f9fa;
                        }
                        .borrowed-item { 
                            border-left-color: #e74c3c; 
                        }
                        .returned-item { 
                            border-left-color: #27ae60; 
                        }
                        .stats { 
                            margin: 20px 0; 
                        }
                        .stat-card { 
                            display: inline-block;
                            background: white; 
                            padding: 15px; 
                            margin: 0 10px 10px 0;
                            border: 1px solid #ddd;
                            border-radius: 4px;
                            text-align: center;
                            min-width: 120px;
                        }
                        .stat-number { 
                            font-size: 24px; 
                            font-weight: bold; 
                            color: #2c3e50; 
                        }
                        .form-group { 
                            margin: 15px 0; 
                        }
                        label { 
                            display: block; 
                            margin-bottom: 5px; 
                            font-weight: bold; 
                        }
                        input, select, textarea { 
                            width: 100%; 
                            padding: 8px; 
                            border: 1px solid #ddd; 
                            border-radius: 3px; 
                            box-sizing: border-box;
                        }
                        button { 
                            background: #3498db; 
                            color: white; 
                            padding: 8px 15px; 
                            border: none; 
                            border-radius: 3px; 
                            cursor: pointer; 
                        }
                        button:hover { 
                            background: #2980b9; 
                        }
                        .success { 
                            background: #d4edda; 
                            color: #155724; 
                            padding: 10px; 
                            border-radius: 3px; 
                            margin: 10px 0;
                            border: 1px solid #c3e6cb;
                        }
                        .error { 
                            background: #f8d7da; 
                            color: #721c24; 
                            padding: 10px; 
                            border-radius: 3px; 
                            margin: 10px 0;
                            border: 1px solid #f5c6cb;
                        }
                        .action-form { 
                            display: inline; 
                        }
                        ul {
                            list-style-type: none;
                            padding-left: 0;
                        }
                        li {
                            margin: 8px 0;
                        }
                        .grid {
                            display: grid;
                            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
                            gap: 15px;
                        }
                    </style>
                </head>
                <body>
                    <h1>Библиотечная система</h1>
                    <div class='nav'>
                        <a href='library?'>Главная</a>
                        <a href='library?action=books'>Все книги</a>
                        <a href='library?action=readers'>Все читатели</a>
                        <a href='library?action=borrowed'>Выданные книги</a>
                        <a href='library?action=addBook'>Добавить книгу</a>
                        <a href='library?action=addReader'>Добавить читателя</a>
                        <a href='library?action=borrowBook'>Выдать книгу</a>
                    </div>
                """;
    }

    private String generateMessageBlocks(HttpServletRequest req) {
        StringBuilder messages = new StringBuilder();
        String message = req.getParameter("message");
        String error = req.getParameter("error");
        if (message != null) {
            messages.append("<div class='success'>").append(message).append("</div>");
        }
        if (error != null) {
            messages.append("<div class='error'>").append(error).append("</div>");
        }
        return messages.toString();
    }

    private String generateFooter() {
        return """
                    </div>
                </body>
                </html>
                """;
    }

    private String generateDashboard() {
        int booksCount = libraryService.getAllBooks().size();
        int readersCount = libraryService.getAllReaders().size();
        int borrowedCount = libraryService.getAllBorrowedBooks().size();
        return """
                <div class='stats'>
                    <div class='stat-card'>
                        <div class='stat-number'>%d</div>
                        <div>Всего книг</div>
                    </div>
                    <div class='stat-card'>
                        <div class='stat-number'>%d</div>
                        <div>Всего читателей</div>
                    </div>
                    <div class='stat-card'>
                        <div class='stat-number'>%d</div>
                        <div>Книг выдано</div>
                    </div>
                </div>
                <div class='card'>
                    <h2>Добро пожаловать в библиотечную систему</h2>
                    <p>Используйте навигацию выше для управления библиотекой.</p>
                </div>
                """.formatted(booksCount, readersCount, borrowedCount);
    }

    private String generateActionContent(String action, HttpServletRequest req) {
        return switch (action) {
            case "books" -> generateAllBooks();
            case "readers" -> generateAllReaders();
            case "borrowed" -> generateAllBorrowedBooks();
            case "readerBooks" -> {
                String readerIdParam = req.getParameter("readerId");
                if (readerIdParam != null) {
                    yield generateBorrowedBooksByReader(Integer.parseInt(readerIdParam));
                }
                yield generateDashboard();
            }
            case "addBook" -> generateAddBookForm();
            case "addReader" -> generateAddReaderForm();
            case "borrowBook" -> generateBorrowBookForm();
            default -> generateDashboard();
        };
    }

    private String generateAllBooks() {
        List<Book> books = libraryService.getAllBooks();
        StringBuilder content = new StringBuilder();
        content.append("""
                <div class='card'>
                    <h2>Все книги (%d)</h2>
                """.formatted(books.size()));
        if (books.isEmpty()) {
            content.append("<p>В библиотеке пока нет книг</p>");
        } else {
            content.append("<div class='grid'>");
            for (Book book : books) {
                content.append("""
                        <div class='book-item'>
                            <h3>%s</h3>
                            <p><strong>Автор:</strong> %s</p>
                            <p><strong>Год:</strong> %d</p>
                            <p><strong>Жанр:</strong> %s</p>
                        </div>
                        """.formatted(escapeHtml(book.getTitle()), escapeHtml(book.getAuthor()),
                        book.getPublishedYear(), escapeHtml(book.getGenre())));
            }
            content.append("</div>");
        }
        content.append("</div>");
        return content.toString();
    }

    private String generateAllReaders() {
        List<Reader> readers = libraryService.getAllReaders();
        StringBuilder content = new StringBuilder();
        content.append("""
                <div class='card'>
                    <h2>Все читатели (%d)</h2>
                """.formatted(readers.size()));
        if (readers.isEmpty()) {
            content.append("<p>В библиотеке пока нет читателей</p>");
        } else {
            content.append("<div class='grid'>");
            for (Reader reader : readers) {
                content.append("""
                        <div class='reader-item'>
                            <h3>%s</h3>
                            <p><strong>Email:</strong> %s</p>
                            <p><strong>Телефон:</strong> %s</p>
                            <p><a href='library?action=readerBooks&readerId=%d'>Показать выданные книги</a></p>
                        </div>
                        """.formatted(escapeHtml(reader.getName()), escapeHtml(reader.getEmail()),
                        escapeHtml(reader.getPhone()), reader.getReaderId()));
            }
            content.append("</div>");
        }
        content.append("</div>");
        return content.toString();
    }

    private String generateBorrowedBooksByReader(int readerId) {
        Optional<Reader> readerOpt = libraryService.getReaderById(readerId);
        if (readerOpt.isEmpty()) {
            return "<div class='error'>Читатель не найден</div>";
        }
        Reader reader = readerOpt.get();
        List<BorrowedBook> borrowedBooks = libraryService.getBorrowedBooksByReaderId(readerId);
        StringBuilder content = new StringBuilder();
        content.append("""
                <div class='card'>
                    <h2>Книги на руках у %s</h2>
                """.formatted(escapeHtml(reader.getName())));
        if (borrowedBooks.isEmpty()) {
            content.append("<p>У читателя нет книг на руках</p>");
        } else {
            content.append("<div class='grid'>");
            for (BorrowedBook borrowedBook : borrowedBooks) {
                Optional<Book> bookOpt = libraryService.getBookById(borrowedBook.getBookId());
                if (bookOpt.isPresent()) {
                    Book book = bookOpt.get();
                    content.append("""
                            <div class='borrowed-item'>
                                <h3>%s</h3>
                                <p><strong>Автор:</strong> %s</p>
                                <p><strong>Дата выдачи:</strong> %s</p>
                            <form method='post' action='library' class='action-form'>
                                <input type='hidden' name='action' value='returnBook'>
                                <input type='hidden' name='borrowId' value='%d'>
                                <button type='submit'>Вернуть книгу</button>
                            </form>
                            </div>
                            """.formatted(escapeHtml(book.getTitle()), escapeHtml(book.getAuthor()),
                            borrowedBook.getBorrowDate(), borrowedBook.getBorrowId()));
                }
            }
            content.append("</div>");
        }
        content.append("</div>");
        return content.toString();
    }

    private String generateAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = libraryService.getAllBorrowedBooks();
        StringBuilder content = new StringBuilder();
        content.append("""
                <div class='card'>
                    <h2>Все выданные книги (%d)</h2>
                """.formatted(borrowedBooks.size()));
        if (borrowedBooks.isEmpty()) {
            content.append("<p>Нет выданных книг</p>");
        } else {
            content.append("<div class='grid'>");
            for (BorrowedBook borrowedBook : borrowedBooks) {
                Optional<Book> bookOpt = libraryService.getBookById(borrowedBook.getBookId());
                Optional<Reader> readerOpt = libraryService.getReaderById(borrowedBook.getReaderId());
                if (bookOpt.isPresent() && readerOpt.isPresent()) {
                    Book book = bookOpt.get();
                    Reader reader = readerOpt.get();
                    String statusClass = "borrowed".equals(borrowedBook.getStatus()) ? "borrowed-item" : "returned-item";
                    String statusText = "borrowed".equals(borrowedBook.getStatus()) ? "На руках" : "Возвращена";
                    content.append("""
                            <div class='%s'>
                                <h3>%s</h3>
                                <p><strong>Автор:</strong> %s</p>
                                <p><strong>Читатель:</strong> %s</p>
                                <p><strong>Дата выдачи:</strong> %s</p>
                                <p><strong>Статус:</strong> %s</p>
                            """.formatted(statusClass, escapeHtml(book.getTitle()), escapeHtml(book.getAuthor()),
                            escapeHtml(reader.getName()), borrowedBook.getBorrowDate(), statusText));
                    if ("borrowed".equals(borrowedBook.getStatus())) {
                        content.append("""
                                <form method='post' action='library' class='action-form' style='display:inline;'>
                                    <input type='hidden' name='action' value='returnBook'>
                                    <input type='hidden' name='borrowId' value='%d'>
                                    <button type='submit'>Вернуть книгу</button>
                                </form>
            """.formatted(borrowedBook.getBorrowId()));
                    }
                    content.append("</div>");
                }
            }
            content.append("</div>");
        }
        content.append("</div>");
        return content.toString();
    }

    private String generateAddBookForm() {
        return """
                <div class='card'>
                    <h2>Добавить новую книгу</h2>
                    <form method='post'>
                        <input type='hidden' name='action' value='addBook'>
                        <div class='form-group'>
                            <label for='title'>Название:</label>
                            <input type='text' id='title' name='title' required>
                        </div>
                        <div class='form-group'>
                            <label for='author'>Автор:</label>
                            <input type='text' id='author' name='author' required>
                        </div>
                        <div class='form-group'>
                            <label for='publishedYear'>Год издания:</label>
                            <input type='number' id='publishedYear' name='publishedYear' required min='1000' max='2030'>
                        </div>
                        <div class='form-group'>
                            <label for='genre'>Жанр:</label>
                            <input type='text' id='genre' name='genre'>
                        </div>
                        <button type='submit'>Добавить книгу</button>
                    </form>
                </div>
                """;
    }

    private String generateAddReaderForm() {
        return """
                <div class='card'>
                    <h2>Добавить нового читателя</h2>
                    <form method='post'>
                        <input type='hidden' name='action' value='addReader'>
                        <div class='form-group'>
                            <label for='name'>Имя:</label>
                            <input type='text' id='name' name='name' required>
                        </div>
                        <div class='form-group'>
                            <label for='email'>Email:</label>
                            <input type='email' id='email' name='email' required>
                        </div>
                        <div class='form-group'>
                            <label for='phone'>Телефон:</label>
                            <input type='tel' id='phone' name='phone'>
                        </div>
                        <button type='submit'>Добавить читателя</button>
                    </form>
                </div>
                """;
    }

    private String generateBorrowBookForm() {
        List<Book> books = libraryService.getAllBooks();
        List<Reader> readers = libraryService.getAllReaders();
        StringBuilder content = new StringBuilder();
        content.append("""
            <div class='card'>
                <h2>Выдать книгу читателю</h2>
                <form method='post'>
                    <input type='hidden' name='action' value='borrowBook'>
                    <div class='form-group'>
                        <label for='bookId'>Книга:</label>
                        <select id='bookId' name='bookId' required>
                            <option value=''>Выберите книгу</option>
            """);
        for (Book book : books) {
            content.append("""
                <option value='%d'>%s — %s</option>
                """.formatted(book.getBookId(), escapeHtml(book.getTitle()), escapeHtml(book.getAuthor())));
        }
        content.append("""
                        </select>
                    </div>
                    <div class='form-group'>
                        <label for='readerId'>Читатель:</label>
                        <select id='readerId' name='readerId' required>
                            <option value=''>Выберите читателя</option>
            """);
        for (Reader reader : readers) {
            content.append("""
                <option value='%d'>%s (%s)</option>
                """.formatted(reader.getReaderId(), escapeHtml(reader.getName()), escapeHtml(reader.getEmail())));
        }
        content.append("""
                        </select>
                    </div>
                    <button type='submit'>Выдать книгу</button>
                </form>
            </div>
            """);
        return content.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private void addBook(HttpServletRequest req) {
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String publishedYearStr = req.getParameter("publishedYear");
        String genre = req.getParameter("genre");

        try {
            int publishedYear = Integer.parseInt(publishedYearStr);
            Book book = new Book(title, author, publishedYear, genre);
            libraryService.addBook(book);
        } catch (NumberFormatException e) {
            throw new LibraryServiceException("Неверный формат года издания: " + publishedYearStr);
        }
    }

    private void addReader(HttpServletRequest req) {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        Reader reader = new Reader(name, email, phone);
        libraryService.addReader(reader);
    }

    private void borrowBook(HttpServletRequest req) {
        String bookIdParam = req.getParameter("bookId");
        String readerIdParam = req.getParameter("readerId");

        System.out.println("DEBUG: bookIdParam = " + bookIdParam + ", readerIdParam = " + readerIdParam);

        if (bookIdParam == null || bookIdParam.trim().isEmpty() ||
                readerIdParam == null || readerIdParam.trim().isEmpty()) {
            throw new LibraryServiceException("Не указан ID книги или читателя. bookId: " + bookIdParam + ", readerId: " + readerIdParam);
        }

        try {
            int bookId = Integer.parseInt(bookIdParam.trim());
            int readerId = Integer.parseInt(readerIdParam.trim());
            libraryService.borrowBook(bookId, readerId);
        } catch (NumberFormatException e) {
            throw new LibraryServiceException("Неверный формат ID книги или читателя. bookId: '" + bookIdParam + "', readerId: '" + readerIdParam + "'");
        }
    }

    private void returnBook(HttpServletRequest req) {
        String borrowIdParam = req.getParameter("borrowId");

        System.out.println("DEBUG: borrowIdParam = " + borrowIdParam);

        if (borrowIdParam == null || borrowIdParam.trim().isEmpty()) {
            throw new LibraryServiceException("Не указан ID записи о выдаче: " + borrowIdParam);
        }

        try {
            int borrowId = Integer.parseInt(borrowIdParam.trim());
            libraryService.returnBook(borrowId);
        } catch (NumberFormatException e) {
            throw new LibraryServiceException("Неверный формат ID записи о выдаче: " + borrowIdParam);
        }
    }

    @Override
    public void destroy() {
        DatabaseConnectionManager.closeConnection(connection);
    }
}