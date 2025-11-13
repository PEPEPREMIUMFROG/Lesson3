package org.example.dao;

import org.example.model.BorrowedBook;
import org.example.model.Book;
import org.example.model.Reader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.util.List;

public class BorrowedBookDAO extends BaseHibernateDAO {

    public List<BorrowedBook> findBorrowedBooksByReaderId(int readerId) {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<BorrowedBook> query = entityManager.createQuery(
                    "SELECT bb FROM BorrowedBook bb " +
                            "JOIN FETCH bb.book " +
                            "JOIN FETCH bb.reader " +
                            "WHERE bb.reader.readerId = :readerId AND bb.status = 'borrowed' " +
                            "ORDER BY bb.borrowDate DESC",
                    BorrowedBook.class
            );
            query.setParameter("readerId", readerId);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding borrowed books for reader: " + readerId, e);
        } finally {
            entityManager.close();
        }
    }

    public List<BorrowedBook> findAllBorrowedBooks() {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<BorrowedBook> query = entityManager.createQuery(
                    "SELECT bb FROM BorrowedBook bb " +
                            "JOIN FETCH bb.book " +
                            "JOIN FETCH bb.reader " +
                            "ORDER BY bb.borrowDate DESC",
                    BorrowedBook.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding all borrowed books", e);
        } finally {
            entityManager.close();
        }
    }

    public BorrowedBook borrowBook(BorrowedBook borrowedBook) {
        return executeInTransaction(entityManager -> {
            Book book = entityManager.find(Book.class, borrowedBook.getBook().getBookId());
            Reader reader = entityManager.find(Reader.class, borrowedBook.getReader().getReaderId());
            if (book == null) {
                throw new DataAccessException("Book not found with id: " + borrowedBook.getBook().getBookId());
            }
            if (reader == null) {
                throw new DataAccessException("Reader not found with id: " + borrowedBook.getReader().getReaderId());
            }
            borrowedBook.setBook(book);
            borrowedBook.setReader(reader);
            entityManager.persist(borrowedBook);
            return borrowedBook;
        });
    }

    public void returnBook(int borrowId) {
        executeInTransaction(entityManager -> {
            BorrowedBook borrowedBook = entityManager.find(BorrowedBook.class, borrowId);
            if (borrowedBook != null) {
                borrowedBook.setStatus("returned");
                borrowedBook.setReturnDate(java.time.LocalDate.now());
                entityManager.merge(borrowedBook);
            } else {
                throw new DataAccessException("Borrowed book not found with id: " + borrowId);
            }
        });
    }

    public List<BorrowedBook> findOverdueBooks() {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<BorrowedBook> query = entityManager.createQuery(
                    "SELECT bb FROM BorrowedBook bb " +
                            "JOIN FETCH bb.book " +
                            "JOIN FETCH bb.reader " +
                            "WHERE bb.status = 'borrowed' AND bb.borrowDate < :overdueDate " +
                            "ORDER BY bb.borrowDate",
                    BorrowedBook.class
            );
            query.setParameter("overdueDate", java.time.LocalDate.now().minusDays(30));
            return query.getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding overdue books", e);
        } finally {
            entityManager.close();
        }
    }
}