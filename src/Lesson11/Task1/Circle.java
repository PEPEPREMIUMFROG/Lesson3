package Lesson11.Task1;

public class Circle implements Figure{

    private static final double PI = Math.PI;
    private double R;


    public Circle(double R) {
        this.R = R;
    }

    @Override
    public double getArea() {
        return Math.pow(R,2)*PI;
    }
}
