package Lesson13;

import java.util.Arrays;

public class DynamicIntArray {

    private int size;
    private int[] array;
    private int capacity;


    public DynamicIntArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.array = new int[capacity];
        this.size = 0;
    }

    public void add(int element) {
        if (size == capacity) {
            ensureCapacity(capacity * 2);
        }
        array[size++] = element;

    }

    public void getByIndex(int index) {
        if (index >= 0 && index < size) {
            System.out.println(array[index-1]);
        } else {
            System.out.println("Index out of bounds");
        }
    }


    private void ensureCapacity(int minCapacity) {
        if (minCapacity > capacity) {
            int newCapacity = capacity * 2;
            int[] newArray = new int[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
            capacity = newCapacity;
        }
    }

    @Override
    public String toString() {
        return "DynamicIntArray{" +
                "size=" + size +
                ", array=" + Arrays.toString(array) +
                ", capacity=" + capacity +
                '}';
    }


    public int getCapacity() {
        return capacity;
    }
}



