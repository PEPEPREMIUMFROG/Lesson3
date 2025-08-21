package Lesson19;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Task2 {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1,2,3,4);
        List<Integer> filteredNums = filterCollection(nums, n -> n%2 ==0, ArrayList::new);
        System.out.printf("Результат выполнения задачи 2: %s\n", filteredNums);
    }
    public static <T, C extends Collection<T>> C filterCollection(
            C collection, Predicate<T> predicate, Supplier<C> collectionFactory
    )
    {
        return collection.stream().filter(predicate).collect(Collectors.toCollection(collectionFactory));
    }

}
