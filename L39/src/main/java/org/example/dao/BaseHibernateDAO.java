package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.example.database.HibernateUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseHibernateDAO {

    private  EntityManager testEntityManager;

    protected    EntityManager getEntityManager() {
        if (testEntityManager != null) {
            return testEntityManager;
        }
        return HibernateUtil.getEntityManagerFactory().createEntityManager();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.testEntityManager = entityManager;
    }

    protected <T> T executeInTransaction(Function<EntityManager, T> function) {
        EntityManager entityManager = getEntityManager();
        boolean shouldClose = (testEntityManager == null);

        try {
            entityManager.getTransaction().begin();
            T result = function.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DataAccessException("Transaction failed: " + e.getMessage(), e);
        } finally {
            if (shouldClose && entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = getEntityManager();
        boolean shouldClose = (testEntityManager == null);

        try {
            entityManager.getTransaction().begin();
            consumer.accept(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new DataAccessException("Transaction failed: " + e.getMessage(), e);
        } finally {
            if (shouldClose && entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected <T> List<T> findAll(Class<T> entityClass) {
        EntityManager entityManager = getEntityManager();
        boolean shouldClose = (testEntityManager == null);

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            cq.from(entityClass);
            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new DataAccessException("Error finding all " + entityClass.getSimpleName(), e);
        } finally {
            if (shouldClose && entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected <T> T findById(Class<T> entityClass, Object id) {
        EntityManager entityManager = getEntityManager();
        boolean shouldClose = (testEntityManager == null);

        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw new DataAccessException("Error finding " + entityClass.getSimpleName() + " by id: " + id, e);
        } finally {
            if (shouldClose && entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}