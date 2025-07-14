package Lesson11.Task1;

public class Square implements Figure{

    private double a;

    public Square(double a) {
        this.a = a;
    }

    @Override
    public double getArea() {
        return a *a;
    }
}
