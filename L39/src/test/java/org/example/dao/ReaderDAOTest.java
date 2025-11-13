package org.example.dao;

import org.example.model.Reader;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReaderDAOTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Reader> typedQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Reader> criteriaQuery;

    @Mock
    private Root<Reader> root;

    @Mock
    private Order order;

    @InjectMocks
    private ReaderDAO readerDAO;

    @BeforeEach
    void setUp() {
        readerDAO.setEntityManager(entityManager);
        lenient().when(entityManager.getTransaction()).thenReturn(transaction);
    }

    @Test
    void findById_Success() {
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("Test Reader");
        reader.setEmail("test@example.com");
        when(entityManager.find(Reader.class, 1)).thenReturn(reader);
        Optional<Reader> result = readerDAO.findById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getReaderId());
        assertEquals("Test Reader", result.get().getName());
        verify(entityManager).find(Reader.class, 1);
    }

    @Test
    void findById_NotFound() {
        when(entityManager.find(Reader.class, 1)).thenReturn(null);
        Optional<Reader> result = readerDAO.findById(1);
        assertFalse(result.isPresent());
        verify(entityManager).find(Reader.class, 1);
    }

    @Test
    void findAll_Success() {
        Reader reader1 = new Reader();
        reader1.setReaderId(1);
        reader1.setName("Reader 1");
        Reader reader2 = new Reader();
        reader2.setReaderId(2);
        reader2.setName("Reader 2");
        List<Reader> expectedReaders = Arrays.asList(reader1, reader2);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Reader.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Reader.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.orderBy(any(Order[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedReaders);
        List<Reader> result = readerDAO.findAll();
        assertEquals(2, result.size());
        assertEquals("Reader 1", result.get(0).getName());
        assertEquals("Reader 2", result.get(1).getName());
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void save_NewReader_Success() {
        Reader reader = new Reader();
        reader.setName("New Reader");
        reader.setEmail("new@example.com");
        Reader savedReader = readerDAO.save(reader);
        verify(entityManager).persist(reader);
        assertEquals(reader, savedReader);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void save_ExistingReader_Success() {
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setName("Updated Reader");
        when(entityManager.merge(reader)).thenReturn(reader);
        Reader savedReader = readerDAO.save(reader);
        verify(entityManager).merge(reader);
        assertEquals(reader, savedReader);
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void findByEmail_Success() {
        Reader reader = new Reader();
        reader.setReaderId(1);
        reader.setEmail("test@example.com");
        when(entityManager.createQuery(anyString(), eq(Reader.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("email", "test@example.com")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList(reader));
        Optional<Reader> result = readerDAO.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(typedQuery).setParameter("email", "test@example.com");
    }

    @Test
    void findByEmail_NotFound() {
        when(entityManager.createQuery(anyString(), eq(Reader.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("email", "notfound@example.com")).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Arrays.asList());
        Optional<Reader> result = readerDAO.findByEmail("notfound@example.com");
        assertFalse(result.isPresent());
        verify(typedQuery).setParameter("email", "notfound@example.com");
    }
}