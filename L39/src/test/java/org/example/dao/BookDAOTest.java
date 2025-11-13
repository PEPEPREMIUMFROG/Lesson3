package org.example.dao;

import org.example.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Book> typedQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Book> criteriaQuery;

    @Mock
    private Root<Book> root;

    @Mock
    private Order order;

    @Captor
    private ArgumentCaptor<CriteriaQuery<Book>> criteriaQueryCaptor;

    @InjectMocks
    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO.setEntityManager(entityManager);
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Test
    void findById_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        when(entityManager.find(Book.class, 1)).thenReturn(book);
        Optional<Book> result = bookDAO.findById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getBookId());
        assertEquals("Test Book", result.get().getTitle());
        verify(entityManager).find(Book.class, 1);
    }

    @Test
    void findById_NotFound() {
        when(entityManager.find(Book.class, 1)).thenReturn(null);
        Optional<Book> result = bookDAO.findById(1);
        assertFalse(result.isPresent());
        verify(entityManager).find(Book.class, 1);
    }

    @Test
    void findAll_Success() {
        Book book1 = new Book();
        book1.setBookId(1);
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setBookId(2);
        book2.setTitle("Book 2");
        List<Book> expectedBooks = Arrays.asList(book1, book2);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Book.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Book.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.orderBy(any(Order[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedBooks);
        List<Book> result = bookDAO.findAll();
        assertEquals(2, result.size());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Book 2", result.get(1).getTitle());
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(Book.class);
        verify(criteriaQuery).from(Book.class);
        verify(criteriaQuery).select(root);
        verify(criteriaQuery).orderBy(any(Order[].class));
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).getResultList();
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void save_NewBook_Success() {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("Author");
        book.setPublishedYear(2023);
        Book savedBook = bookDAO.save(book);
        verify(entityManager).persist(book);
        assertEquals(book, savedBook);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void save_ExistingBook_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Updated Book");
        when(entityManager.merge(book)).thenReturn(book);
        Book savedBook = bookDAO.save(book);
        verify(entityManager).merge(book);
        assertEquals(book, savedBook);
        assertNotNull(savedBook);
        assertEquals("Updated Book", savedBook.getTitle());
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void findByTitle_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("Test Book");
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("title", "%test%")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(book));
        List<Book> result = bookDAO.findByTitle("test");
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(typedQuery).setParameter("title", "%test%");
        verify(entityManager).close();
    }

    @Test
    void findByAuthor_Success() {
        Book book = new Book();
        book.setBookId(1);
        book.setAuthor("Test Author");
        when(entityManager.createQuery(anyString(), eq(Book.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("author", "%author%")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(book));
        List<Book> result = bookDAO.findByAuthor("author");
        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getAuthor());
        verify(typedQuery).setParameter("author", "%author%");
        verify(entityManager).close();
    }
}