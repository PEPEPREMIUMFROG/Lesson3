package org.example;

import org.example.config.DataSourceConfig;
import org.example.dao.AuthorDAO;
import org.example.dao.BookDAO;
import org.example.model.Author;
import org.example.model.Book;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@Configuration
@ComponentScan(basePackages = "org.example")
public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class, DataSourceConfig.class);
        AuthorDAO authorDao = context.getBean(AuthorDAO.class);
        BookDAO bookDao = context.getBean(BookDAO.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);


        System.out.println("\n1. Testing author operations");
        Author newAuthor = new Author("J.R.R. Tolkien", "UK");
        Author savedAuthor = authorDao.save(newAuthor);
        System.out.println("Author created: " + savedAuthor);
        Optional<Author> foundAuthor = authorDao.findById(savedAuthor.getId());
        foundAuthor.ifPresent(author -> System.out.println("Author found by ID: " + author));
        savedAuthor.setName("John Ronald Reuel Tolkien");
        authorDao.update(savedAuthor);
        System.out.println("Author updated: " + authorDao.findById(savedAuthor.getId()).orElse(null));


        System.out.println("\n2. Testing book operations");
        Book newBook = new Book("The Hobbit", 1937, savedAuthor.getId());
        Book savedBook = bookDao.save(newBook);
        System.out.println("Book created: " + savedBook);
        Optional<Book> foundBook = bookDao.findById(savedBook.getId());
        foundBook.ifPresent(book -> System.out.println("Book found by ID: " + book));
        List<Book> authorsBooks = bookDao.findByAuthorId(savedAuthor.getId());
        System.out.println("Books by author " + savedAuthor.getName() + ":");
        authorsBooks.forEach(System.out::println);


        System.out.println("\n3. Testing search functionality");
        List<Author> tolkienAuthors = authorDao.findByName("Tolkien");
        System.out.println("Authors with name 'Tolkien':");
        tolkienAuthors.forEach(System.out::println);
        List<Book> hobbitBooks = bookDao.findByTitle("Hobbit");
        System.out.println("Books with title 'Hobbit':");
        hobbitBooks.forEach(System.out::println);

        System.out.println("\n4. Testing delete functionality");
        bookDao.delete(savedBook.getId());
        System.out.println("Book deleted. Existence check: " +
                bookDao.findById(savedBook.getId()).isPresent());
        authorDao.delete(savedAuthor.getId());
        System.out.println("Author deleted. Existence check: " +
                authorDao.findById(savedAuthor.getId()).isPresent());


        System.out.println("\n5. Validate data");
        List<Author> allAuthors = authorDao.findAll();
        System.out.println("All authors in database:");
        allAuthors.forEach(System.out::println);
        List<Book> allBooks = bookDao.findAll();
        System.out.println("All books in database:");
        allBooks.forEach(System.out::println);

        System.out.println("\nTesting completed successfully!");
        ((AnnotationConfigApplicationContext) context).close();
    }
}