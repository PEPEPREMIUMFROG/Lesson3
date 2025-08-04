package Lesson16.iprody.coffeeshot;

import java.util.LinkedHashMap;

public class CoffeeOrderBoard {
    private int nextNumber = 1;
    private final LinkedHashMap<Integer, Order> orders = new LinkedHashMap<>();

    public void add(String nameOfClient) {
        orders.put(nextNumber,(new Order(nextNumber++, nameOfClient)));
    }

    public void deliver() {
        System.out.printf("Выдан заказ %s\n", orders.pollFirstEntry());
    }

    public void deliver(int numberOfOrder) {
        System.out.printf("Выдан заказ %s\n", orders.remove(numberOfOrder));
    }

    public void draw() {
        System.out.println("\nNum | Name");
        for (Order order : orders.values()) {
            System.out.printf("  %s | %s \n", order.number(), order.nameOfClient());
        }
        System.out.println();
    }

}
