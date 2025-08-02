package Lesson14;

import java.util.*;

public class ListUtils {

    public static void main(String[] args) {
        List<String> words = new ArrayList<>(Arrays.asList("apple", "banana", "apple", "orange"));

        System.out.println(countOccurance(words, "apple"));

        int[] arr = {7, 3, 5};
        List<Integer> nums = toList(arr);
        System.out.println(nums);

        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 1, 3));
        System.out.println(findUnique(numbers));

        calcOccurance(words);

        List<OccurrenceInfo> occurrences = findOccurance(words);
        System.out.println(occurrences);
    }

    public static int countOccurance(List<String> list, String target) {
        int count = 0;
        for (String word : list) {
            if (word.equals(target)) {
                count++;
            }
        }
        return count;
    }

    public static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        return list;
    }

    public static List<Integer> findUnique(List<Integer> list) {
        List<Integer> unique = new ArrayList<>();
        for (Integer num : list) {
            if (!unique.contains(num)) {
                unique.add(num);
            }
        }
        return unique;
    }

    public static void calcOccurance(List<String> list) {
        List<OccurrenceInfo> result = findOccurance(list);
        for (OccurrenceInfo info : result) {
            System.out.printf("%s: %d, ", info.getName(), info.getOccurrence());
        }
        System.out.println();
    }

    public static List<OccurrenceInfo> findOccurance(List<String> list) {

        Collections.sort(list);
        List<OccurrenceInfo> result = new ArrayList<>();
        String prev = list.get(0);
        int count = 1;
        for (int i = 1; i <= list.size(); ++i) {
            if (i == list.size() || !prev.equals(list.get(i))) {
                result.add(new OccurrenceInfo(prev, count));
                if (i != list.size()) {
                    prev = list.get(i);
                    count = 1;
                }
            } else {
                count++;
            }
        }
        return result;
    }

    public static class OccurrenceInfo {
        private final String name;
        private final int occurrence;

        public OccurrenceInfo(String name, int occurrence) {
            this.name = name;
            this.occurrence = occurrence;
        }

        public String getName() {
            return name;
        }

        public int getOccurrence() {
            return occurrence;
        }

        @Override
        public String toString() {
            return "{name: \"" + name + "\", occurrence: " + occurrence + "}";
        }
    }
}