package org.example.dao;

import org.example.model.Book;
import org.example.model.BorrowedBook;
import org.example.model.Reader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowedBookDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<BorrowedBook> typedQuery;

    @InjectMocks
    private BorrowedBookDAO borrowedBookDAO;

    @BeforeEach
    void setUp() {
        borrowedBookDAO.setEntityManager(entityManager);
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
        lenient().when(transaction.isActive()).thenReturn(true);
    }

    @Test
    void findBorrowedBooksByReaderId_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("Test Reader");
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(1);
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setStatus("borrowed");
        when(entityManager.createQuery(anyString(), eq(BorrowedBook.class))).thenReturn(typedQuery);
        lenient().when(typedQuery.setParameter("readerId", 1)).thenReturn(typedQuery);
        lenient().when(typedQuery.getResultList()).thenReturn(Arrays.asList(borrowedBook));
        List<BorrowedBook> result = borrowedBookDAO.findBorrowedBooksByReaderId(1);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBorrowId());
        assertEquals("borrowed", result.get(0).getStatus());
        verify(typedQuery).setParameter("readerId", 1);
    }

    @Test
    void findAllBorrowedBooks_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("Test Reader");
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(1);
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        when(entityManager.createQuery(anyString(), eq(BorrowedBook.class))).thenReturn(typedQuery);
        lenient().when(typedQuery.getResultList()).thenReturn(Arrays.asList(borrowedBook));
        List<BorrowedBook> result = borrowedBookDAO.findAllBorrowedBooks();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBorrowId());
    }

    @Test
    void borrowBook_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("Test Reader");
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setStatus("borrowed");
        when(entityManager.find(Book.class, 1)).thenReturn(book);
        when(entityManager.find(Reader.class, 1)).thenReturn(reader);
        BorrowedBook result = borrowedBookDAO.borrowBook(borrowedBook);
        verify(entityManager).persist(any(BorrowedBook.class));
        verify(transaction).begin();
        verify(transaction).commit();
        assertNotNull(result);
        assertNotNull(result.getBook());
        assertNotNull(result.getReader());
        assertEquals("borrowed", result.getStatus());
    }

    @Test
    void borrowBook_BookNotFound() {
        Book book = new Book();
        book.setBookId(1);
        Reader reader = new Reader();
        reader.setReaderId(1);
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        when(entityManager.find(Book.class, 1)).thenReturn(null);
        when(entityManager.find(Reader.class, 1)).thenReturn(reader);
        assertThrows(DataAccessException.class, () -> {
  borrowedBookDAO.borrowBook(borrowedBook);
        });
        verify(transaction).begin();
        verify(transaction).rollback();
        verify(transaction, never()).commit();
    }

    @Test
    void returnBook_Success() {
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(1);
        borrowedBook.setStatus("borrowed");
        when(entityManager.find(BorrowedBook.class, 1)).thenReturn(borrowedBook);
        borrowedBookDAO.returnBook(1);
        verify(entityManager).merge(borrowedBook);
        assertEquals("returned", borrowedBook.getStatus());
        assertNotNull(borrowedBook.getReturnDate());
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void returnBook_NotFound() {
        when(entityManager.find(BorrowedBook.class, 999)).thenReturn(null);
        assertThrows(DataAccessException.class, () -> {
  borrowedBookDAO.returnBook(999);
        });
        verify(transaction).begin();
        verify(transaction).rollback();
        verify(transaction, never()).commit();
    }

    @Test
    void findOverdueBooks_Success() {
        Book book = new Book();
        book.setBookId(1);
        Reader reader = new Reader();
        reader.setReaderId(1);
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBorrowId(1);
        borrowedBook.setBook(book);
        borrowedBook.setReader(reader);
        borrowedBook.setBorrowDate(LocalDate.now().minusDays(31));
        borrowedBook.setStatus("borrowed");
        when(entityManager.createQuery(anyString(), eq(BorrowedBook.class))).thenReturn(typedQuery);
        lenient().when(typedQuery.setParameter("overdueDate", LocalDate.now().minusDays(30))).thenReturn(typedQuery);
        lenient().when(typedQuery.getResultList()).thenReturn(Arrays.asList(borrowedBook));
        List<BorrowedBook> result = borrowedBookDAO.findOverdueBooks();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBorrowId());
        verify(typedQuery).setParameter("overdueDate", LocalDate.now().minusDays(30));
    }
}