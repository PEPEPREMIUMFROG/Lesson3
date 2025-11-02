package models;

import org.example.model.BorrowedBook;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BorrowedBookTest {

    @Test
    void testBorrowedBookCreationAndSetters() {
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(1);
        borrowedBook.setBookId(1);
        borrowedBook.setReaderId(1);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setReturnDate(LocalDate.now().plusDays(14));
        borrowedBook.setStatus("borrowed");
        assertEquals(1, borrowedBook.getBorrowId());
        assertEquals(1, borrowedBook.getBookId());
        assertEquals(1, borrowedBook.getReaderId());
        assertEquals(LocalDate.now(), borrowedBook.getBorrowDate());
        assertEquals(LocalDate.now().plusDays(14), borrowedBook.getReturnDate());
        assertEquals("borrowed", borrowedBook.getStatus());
    }

    @Test
    void testBorrowedBookConstructor() {
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(14);
        BorrowedBook borrowedBook = new BorrowedBook(1, 1, borrowDate, returnDate, "borrowed");
        assertEquals(1, borrowedBook.getBookId());
        assertEquals(1, borrowedBook.getReaderId());
        assertEquals(borrowDate, borrowedBook.getBorrowDate());
        assertEquals(returnDate, borrowedBook.getReturnDate());
        assertEquals("borrowed", borrowedBook.getStatus());
        assertNull(borrowedBook.getBorrowId());
    }
}