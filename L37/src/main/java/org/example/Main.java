package org.example;

public class Main {
    public static void main(String[] args) {
        CoffeeOrderBoard board = new CoffeeOrderBoard();
        board.add("Ivan");
        board.add("Andrew");
        board.add("Alex");
        board.draw();
        Order delivered1 = board.deliver();
        System.out.println("Delivered (FIFO): " + (delivered1 != null ? delivered1.getCustomerName() : "None"));
        board.add("Vladimir");
        board.draw();
        Order delivered2 = board.deliver(3);
        System.out.println("Delivered (by number 3): " + (delivered2 != null ? delivered2.getCustomerName() : "None"));
        board.draw();
        Order delivered3 = board.deliver();
        System.out.println("Delivered (FIFO): " + (delivered3 != null ? delivered3.getCustomerName() : "None"));
        Order delivered4 = board.deliver();
        System.out.println("Delivered (FIFO): " + (delivered4 != null ? delivered4.getCustomerName() : "None"));
        Order delivered5 = board.deliver();
        System.out.println("Delivered (from empty queue): " + (delivered5 != null ? delivered5.getCustomerName() : "None"));
        board.draw();
    }
}