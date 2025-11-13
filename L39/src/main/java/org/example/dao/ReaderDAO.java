package org.example.dao;

import org.example.model.Reader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

public class ReaderDAO extends BaseHibernateDAO {

    public Optional<Reader> findById(int readerId) {
        try {
            Reader reader = findById(Reader.class, readerId);
            return Optional.ofNullable(reader);
        } catch (Exception e) {
            throw new DataAccessException("Error finding reader by id: " + readerId, e);
        }
    }

    public List<Reader> findAll() {
        try {
            return executeInTransaction(entityManager -> {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Reader> cq = cb.createQuery(Reader.class);
                Root<Reader> root = cq.from(Reader.class);
                cq.select(root).orderBy(cb.asc(root.get("name")));
                return entityManager.createQuery(cq).getResultList();
            });
        } catch (Exception e) {
            throw new DataAccessException("Error finding all readers", e);
        }
    }

    public Reader save(Reader reader) {
        return executeInTransaction(entityManager -> {
            if (reader.getReaderId() == null) {
                entityManager.persist(reader);
                return reader;
            } else {
                return entityManager.merge(reader);
            }
        });
    }

    public Optional<Reader> findByEmail(String email) {
        EntityManager entityManager = getEntityManager();
        try {
            TypedQuery<Reader> query = entityManager.createQuery(
                    "SELECT r FROM Reader r WHERE r.email = :email",
                    Reader.class
            );
            query.setParameter("email", email);
            List<Reader> readers = query.getResultList();
            return readers.isEmpty() ? Optional.empty() : Optional.of(readers.get(0));
        } catch (Exception e) {
            throw new DataAccessException("Error finding reader by email: " + email, e);
        } finally {
            entityManager.close();
        }
    }
}