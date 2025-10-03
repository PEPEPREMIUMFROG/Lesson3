package org.example;

public class ArrayUtils {
    public static int sumArr(int[] arr) {
        if (arr != null && arr.length != 0) {
            int sum = 0;

            for(int num : arr) {
                sum += num;
            }

            return sum;
        } else {
            throw new IllegalArgumentException("Arr must be not null");
        }
    }

    public static double average(int[] arr) {
        if (arr != null && arr.length != 0) {
            int total = sumArr(arr);
            return (double)total / (double)arr.length;
        } else {
            throw new IllegalArgumentException("Arr must be not null");
        }
    }

    public static int max(int[] arr) {
        if (arr != null && arr.length != 0) {
            int max = arr[0];

            for(int num : arr) {
                if (num > max) {
                    max = num;
                }
            }

            return max;
        } else {
            throw new IllegalArgumentException("Arr must be not null");
        }
    }

    public static int min(int[] arr) {
        if (arr != null && arr.length != 0) {
            int min = arr[0];

            for(int num : arr) {
                if (num < min) {
                    min = num;
                }
            }

            return min;
        } else {
            throw new IllegalArgumentException("Arr must be not null");
        }
    }
}
