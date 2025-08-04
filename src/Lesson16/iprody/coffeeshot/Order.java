package Lesson16.iprody.coffeeshot;

public record Order(int number, String nameOfClient) {

    @Override
    public String toString() {
        return "Order{" +
                "number=" + number +
                ", nameOfClient='" + nameOfClient + '\'' +
                '}';
    }
}
