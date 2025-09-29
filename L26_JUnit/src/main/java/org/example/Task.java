package org.example;


import java.util.Arrays;
import java.util.stream.IntStream;

public class Task {

    private int[] methodOne(int[] arr) {
        int indexOfLastFour = -1;
        int counter = 0;

        while (counter < arr.length) {
            if (arr[counter] == 4) {
                indexOfLastFour = counter;
            }
            counter++;
        }

        if (indexOfLastFour == -1) {
            throw new RuntimeException("array dont have 4");
        }

        int newLength = arr.length - indexOfLastFour - 1;
        int[] newArr = new int[newLength];

        for (int i = 0; i < newLength; i++) {
            newArr[i] = arr[i + indexOfLastFour + 1];
        }

        return newArr;
    }

    public int[] methoOneWithStreams(int[] arr) {
        int lastIndexOfFour = IntStream.range(0, arr.length)
                .filter(i -> arr[i] == 4)
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("array dont have 4"));

        return Arrays.stream(arr, lastIndexOfFour + 1, arr.length)
                .toArray();
    }


    public boolean methodTwo(int[] arr) {
        if (arr == null || arr.length < 2) {
            return false;
        }
        return Arrays.stream(arr).allMatch(i -> i == 1 || i == 4) &&
                Arrays.stream(arr).anyMatch(i -> i == 1) &&
                Arrays.stream(arr).anyMatch(i -> i == 4);

    }


}