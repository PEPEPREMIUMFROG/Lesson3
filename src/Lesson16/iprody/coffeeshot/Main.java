package Lesson16.iprody.coffeeshot;

public class Main {
    public static void main(String[] args) {
        CoffeeOrderBoard coffeeOrderBoard = new CoffeeOrderBoard();

        coffeeOrderBoard.add("John");
        coffeeOrderBoard.add("Jane");
        coffeeOrderBoard.add("Jacob");
        coffeeOrderBoard.add("Jul");
        coffeeOrderBoard.add("Jiovanni");

        coffeeOrderBoard.draw();

        coffeeOrderBoard.deliver();
        coffeeOrderBoard.deliver();

        coffeeOrderBoard.draw();

        coffeeOrderBoard.add("Andrew");
        coffeeOrderBoard.add("Austin");
        coffeeOrderBoard.add("April");
        coffeeOrderBoard.add("Amadest");

        coffeeOrderBoard.deliver(4);
        coffeeOrderBoard.deliver(7);

        coffeeOrderBoard.draw();
    }
}
