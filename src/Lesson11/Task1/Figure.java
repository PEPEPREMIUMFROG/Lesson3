package Lesson11.Task1;

import java.io.PrintStream;

public interface Figure {

    double getArea();

    default void printArea(PrintStream outStream){
        outStream.println(getArea());
    }
}
