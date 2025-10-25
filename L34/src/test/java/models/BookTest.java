package models;
import org.example.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreationAndSetters() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublishedYear(2023);
        book.setGenre("Fiction");
        assertEquals(1, book.getBookId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2023, book.getPublishedYear());
        assertEquals("Fiction", book.getGenre());
    }

    @Test
    void testBookConstructor() {
        Book book = new Book("Constructor Book", "Constructor Author", 2024, "Science");
        assertEquals("Constructor Book", book.getTitle());
        assertEquals("Constructor Author", book.getAuthor());
        assertEquals(2024, book.getPublishedYear());
        assertEquals("Science", book.getGenre());
        assertNull(book.getBookId());
    }
}