package Lesson18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task1 {
    public static void main(String[] args) {
        Object[] firstTestArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Object[] secondTestArr = {'a', 'b', 'c', 'd', 1, 2, 'e'};
        swapPairs(firstTestArr);
        System.out.println(Arrays.toString(firstTestArr));
        swapPairs(secondTestArr);
        System.out.println(Arrays.toString(secondTestArr));
        Object[] nullArr = null;
        swapPairs(nullArr);
        System.out.println(Arrays.toString(nullArr));
        Object[] emptyArr = {};
        swapPairs(emptyArr);
        System.out.println(Arrays.toString(emptyArr));


        List<Object> firstList = convertArrToList(firstTestArr);
        List<Object> secondList = convertArrToList(secondTestArr);
        List<Object> nullList = convertArrToList(nullArr);
        List<Object> emptyList = convertArrToList(emptyArr);
        System.out.println(secondList);
    }

    private static <T> void swapPairs(T[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i += 2) {
            T temp = arr[i];
            arr[i] = arr[i + 1];
            arr[i + 1] = temp;
        }
    }

    private static <T> List<T> convertArrToList(T[] arr) {
        if (arr == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(arr));
    }

}


