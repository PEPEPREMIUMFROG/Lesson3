package org.example.config;

import org.example.database.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationLifecycleManager implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing Hibernate EntityManagerFactory...");
        HibernateUtil.getEntityManagerFactory();
        System.out.println("Hibernate EntityManagerFactory initialized successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down Hibernate EntityManagerFactory...");
        HibernateUtil.shutdown();
        System.out.println("Hibernate EntityManagerFactory shut down successfully");
    }
}