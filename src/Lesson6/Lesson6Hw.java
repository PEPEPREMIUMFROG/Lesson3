package Lesson6;

import java.util.Arrays;

public class Lesson6Hw {
    public static void main(String[] args) {

        int[] source = {1, 2, 3, 4};
        int[] target = {};
        int[] result1 = arrayCopy(source, target);
        System.out.println(Arrays.toString(result1));

        target = new int[]{5, 6, 7};

        int[] result2 = arrayCopy(source, target);
        System.out.println(Arrays.toString(result2));

        shakeSort(result2);
        System.out.println(Arrays.toString(result2));

    }


    public static int[] arrayCopy(int[] source, int[] target) {
        int resultLength = source.length + target.length;
        int[] result = new int[resultLength];

        if (target.length > 0) {
            for (int i = 0; i < target.length; i++) {
                result[i] = target[i];
            }
        }
        for (int i = 0; i < source.length; i++) {
            result[target.length + i] = source[i];
        }
        return result;
    }

    public static void shakeSort(int[] arr) {
        if (arr.length <= 1) {
            return;
        }

        boolean swapped = true;
        int left = 0;
        int right = arr.length - 1;

        while (swapped) {
            swapped = false;

            for (int i = left; i < right; ++i) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
            swapped = false;
            --right;

            for (int i = right - 1; i >= left; --i) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapped = true;
                }
            }
            ++left;
        }
    }
}
