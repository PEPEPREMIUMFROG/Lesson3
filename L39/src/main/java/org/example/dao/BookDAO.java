package org.example.dao;

import org.example.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public class BookDAO extends BaseHibernateDAO {

    public Optional<Book> findById(int bookId) {
        try {
            Book book = findById(Book.class, bookId);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            throw new DataAccessException("Error finding book by id: " + bookId, e);
        }
    }

    public List<Book> findAll() {
        try {
            return executeInTransaction(entityManager -> {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Book> cq = cb.createQuery(Book.class);
                Root<Book> root = cq.from(Book.class);
                cq.select(root).orderBy(cb.asc(root.get("title")));
                return entityManager.createQuery(cq).getResultList();
            });
        } catch (Exception e) {
            throw new DataAccessException("Error finding all books", e);
        }
    }

    public Book save(Book book) {
        return executeInTransaction(entityManager -> {
            if (book.getBookId() == null) {
                entityManager.persist(book);
                return book;
            } else {
                return entityManager.merge(book);
            }
        });
    }

    public List<Book> findByTitle(String title) {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<Book> query = entityManager.createQuery(
                    "SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(:title) ORDER BY b.title",
                    Book.class
            );
            query.setParameter("title", "%" + title + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding books by title: " + title, e);
        } finally {
            entityManager.close();
        }
    }

    public List<Book> findByAuthor(String author) {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<Book> query = entityManager.createQuery(
                    "SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(:author) ORDER BY b.title",
                    Book.class
            );
            query.setParameter("author", "%" + author + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding books by author: " + author, e);
        } finally {
            entityManager.close();
        }
    }
}