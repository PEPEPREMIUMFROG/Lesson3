package Lesson11.Task1;

public class AreaCalculator {
    public static double calculateTotalArea(Figure[] figures){
        double totalArea = 0;
        for (Figure figure : figures){
           totalArea += figure.getArea();
        }
        return totalArea;
    }

    
}
