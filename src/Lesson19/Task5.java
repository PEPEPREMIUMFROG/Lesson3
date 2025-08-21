package Lesson19;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class Task5 {
    public static void main(String[] args) {
        System.out.printf("Факториал числа 3: %s\n", calculateFactorial(3));
        System.out.printf("Факториал числа 30: %s\n", calculateFactorial(30));
        System.out.printf("Факториал числа 300: %s\n", calculateFactorial(300));
    }

    public static BigInteger calculateFactorial(Integer num) {
        return IntStream.rangeClosed(1, num)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }
}
