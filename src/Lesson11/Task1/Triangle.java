package Lesson11.Task1;

public class Triangle implements Figure {
    private double a;
    private Double h;
    private double b;
    private double c;

    public Triangle(double a, double h) {
        this.a = a;
        this.h = h;
    }

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }


    @Override
    public double getArea() {
        if (h != null) {
            return 0.5 * a * h;
        } else {
            double p = (a + b + c) / 2;
            return Math.sqrt(p * (p - a) * (p - b) * (p - c));
        }
    }
}
