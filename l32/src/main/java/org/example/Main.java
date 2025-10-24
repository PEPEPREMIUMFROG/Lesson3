package org.example;

import org.example.dao.NetworkDao;
import org.example.service.NetworksService;
import org.example.ui.UiController;
import org.example.util.DatabaseInitializer;
import org.example.util.DevDatabaseManager;

import java.util.Scanner;



public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        if (System.getProperty("testcontainers") == null) {
            DevDatabaseManager.start();
            System.setProperty("db.url", DevDatabaseManager.getJdbcUrl());
            System.setProperty("db.username", DevDatabaseManager.getUsername());
            System.setProperty("db.password", DevDatabaseManager.getPassword());
        }

        DatabaseInitializer.initializeDatabase();
        Scanner scanner = new Scanner(System.in);
        var uiController = new UiController(scanner, System.out);
        var networkDao = new NetworkDao();
        var networkService = new NetworksService(uiController, networkDao);
        networkService.process();
    }
}