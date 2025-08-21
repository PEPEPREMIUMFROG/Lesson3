package Lesson19;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Task1 {
    public static void main(String[] args) {
        processNums();
    }
    
    public static void processNums() {
        List<Integer> nums = Stream.generate(() -> new Random().nextInt(1000) + 1)
                .limit(100)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.printf("Результат выполнения задачи 1: %s\n", nums);
    }
}
