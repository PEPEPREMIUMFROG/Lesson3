package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CoffeeOrderBoard {
    private static final Logger logger = LogManager.getLogger(CoffeeOrderBoard.class);
    private Queue<Order> orderQueue;
    private Map<Integer, Order> orderMap;
    private int nextOrderNumber;

    public CoffeeOrderBoard() {
        this.orderQueue = new LinkedList<>();
        this.orderMap = new HashMap<>();
        this.nextOrderNumber = 1;
        logger.info("CoffeeOrderBoard initialized.");
    }


    public void add(String customerName) {
        Order newOrder = new Order(nextOrderNumber, customerName);
        orderQueue.add(newOrder);
        orderMap.put(nextOrderNumber, newOrder);
        logger.info("Added new order: {}", newOrder);
        nextOrderNumber++;
    }


    public Order deliver() {
        Order deliveredOrder = orderQueue.poll();
        if (deliveredOrder != null) {
            orderMap.remove(deliveredOrder.getOrderNumber());
            logger.info("Delivered order from queue: {}", deliveredOrder);
            return deliveredOrder;
        } else {
            logger.warn("Attempted to deliver from an empty queue.");
            return null;
        }
    }


    public Order deliver(int orderNumber) {
        Order orderToDeliver = orderMap.get(orderNumber);
        if (orderToDeliver != null) {
            orderMap.remove(orderNumber);
            if (orderQueue.peek() != null && orderQueue.peek().getOrderNumber() == orderNumber) {
                orderQueue.poll();
            } else {

                orderQueue.removeIf(order -> order.getOrderNumber() == orderNumber);
            }
            logger.info("Delivered specific order: {}", orderToDeliver);
            return orderToDeliver;
        } else {
            logger.warn("Attempted to deliver non-existent order number: {}", orderNumber);
            return null;
        }
    }


    public void draw() {
        logger.info("Drawing current order queue state.");
        System.out.println("-------------");
        System.out.println("Num | Name");
        System.out.println("-------------");
        int index = 1;
        for (Order order : orderQueue) {
            System.out.printf("%-4d| %s%n", index, order.getCustomerName());
            index++;
        }
        System.out.println("-------------");
    }

    public int getNextOrderNumber() {
        return nextOrderNumber;
    }

    public int getQueueSize() {
        return orderQueue.size();
    }
}