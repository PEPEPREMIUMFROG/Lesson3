package Lesson11.Task1;

public class Main {
    public static void main(String[] args) {
        Figure square = new Square(12.5);
        square.printArea(System.out);

        Figure circle = new Circle(4.2);
        circle.printArea(System.out);


        Figure triangleFirst = new Triangle(10, 5);
        triangleFirst.printArea(System.out);

        Figure triangleSecond = new Triangle(10, 5, 10);
        triangleSecond.printArea(System.out);

        Figure[] figures = new Figure[] { circle, square, triangleSecond, triangleFirst};
        double totalArea = AreaCalculator.calculateTotalArea(figures);
        System.out.println("Суммарная площадь всех фигур: " + totalArea);



    }
}
