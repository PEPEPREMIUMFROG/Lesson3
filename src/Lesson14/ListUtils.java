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
            if (!contains(unique, num)) {
                unique.add(num);
            }
        }
        return unique;
    }

    private static boolean contains(List<Integer> list, int target) {
        for (Integer num : list) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

    public static void calcOccurance(List<String> list) {
        List<String> uniqueWords = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (String word : list) {
            int index = indexOf(uniqueWords, word);
            if (index == -1) {
                uniqueWords.add(word);
                counts.add(1);
            } else {
                counts.set(index, counts.get(index) + 1);
            }
        }

        for (int i = 0; i < uniqueWords.size(); i++) {
            System.out.println(uniqueWords.get(i) + ": " + counts.get(i));
        }
    }

    public static List<OccurrenceInfo> findOccurance(List<String> list) {
        List<String> uniqueWords = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<OccurrenceInfo> result = new ArrayList<>();

        for (String word : list) {
            int index = indexOf(uniqueWords, word);
            if (index == -1) {
                uniqueWords.add(word);
                counts.add(1);
            } else {
                counts.set(index, counts.get(index) + 1);
            }
        }

        for (int i = 0; i < uniqueWords.size(); i++) {
            result.add(new OccurrenceInfo(uniqueWords.get(i), counts.get(i)));
        }
        return result;
    }

    private static int indexOf(List<String> list, String target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    public static class OccurrenceInfo {
        private final String name;
        private final int occurrence;

        public OccurrenceInfo(String name, int occurrence) {
            this.name = name;
            this.occurrence = occurrence;
        }

        @Override
        public String toString() {
            return "{name: \"" + name + "\", occurrence: " + occurrence + "}";
        }
    }
}