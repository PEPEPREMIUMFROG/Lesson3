package org.example.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {
    private static final String PERSISTENCE_UNIT_NAME = "library-persistence-unit";
    private static EntityManagerFactory entityManagerFactory;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                Map<String, String> properties = new HashMap<>();
                String dbUrl = System.getenv().getOrDefault("DB_URL",
                        "jdbc:postgresql://postgres:5432/library");
                String dbUser = System.getenv().getOrDefault("DB_USER", "library_user");
                String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "library_password");
                properties.put("jakarta.persistence.jdbc.url", dbUrl);
                properties.put("jakarta.persistence.jdbc.user", dbUser);
                properties.put("jakarta.persistence.jdbc.password", dbPassword);
                entityManagerFactory = Persistence.createEntityManagerFactory(
                        PERSISTENCE_UNIT_NAME, properties);
                System.out.println("Hibernate EntityManagerFactory создан успешно");
                System.out.println("Database URL: " + dbUrl);
            } catch (Exception e) {
                System.err.println("Error when creates EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error when inititalize Hibernate", e);
            }
        }
        return entityManagerFactory;
    }

    public static synchronized void shutdown() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
            System.out.println("Hibernate EntityManagerFactory closed ");
        }
    }
}