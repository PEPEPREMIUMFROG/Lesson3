package Lesson19;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Task4 {
    public static void main(String[] args) {
        List<Integer> ints = List.of(6,6, 8, 3 , 7, 9 ,0 ,0 ,1, 2, 3, 4, 5,7);
        System.out.printf("Отсортировано по возрастанию: %s\n", sortUniqueInts(ints, SortDirection.ASC));
        System.out.printf("Отсортировано по убыванию: %s", sortUniqueInts(ints, SortDirection.DESC));
    }
    public static List<Integer> sortUniqueInts(Collection<Integer> nums, SortDirection sortDirection){
        return nums.stream()
                .distinct()
                .sorted(sortDirection.getComparator())
                .toList();
    }

    enum SortDirection {
        ASC(Comparator.naturalOrder()),
        DESC(Comparator.reverseOrder());

        private final Comparator<Integer> comparator;

        SortDirection(Comparator<Integer> comparator1) {
            this.comparator = comparator1;
        }

        public Comparator<Integer> getComparator() {
            return comparator;
        }
    }


}
