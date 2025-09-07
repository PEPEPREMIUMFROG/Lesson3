package Lesson21;

public abstract class ValueCalculator<T> {
    private static final int DEFAULT_SIZE = 1_000_000;
    private static final int NANO_IN_MILLI = 1_000_000;
    private T[] arr;
    protected final int arrSize;
    protected final String typeName;

    protected ValueCalculator(int arrSize, String typeName) {
        if (arrSize < DEFAULT_SIZE) {
            throw new IllegalArgumentException("Array size must be higher than " + DEFAULT_SIZE);
        }
        this.arrSize = arrSize;
        this.typeName = typeName;
        this.arr = createArr(arrSize);
    }

    public void doCalc() {
        long start = System.nanoTime();
        fillArrayWithOnes();
        int halfSize = arrSize / 2;
        Thread t1 = new Thread(() -> calculateArray(0, halfSize));
        Thread t2 = new Thread(() -> calculateArray(halfSize, arrSize));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        long elapsedTime = (end - start) / NANO_IN_MILLI;
        System.out.println("Elapsed time for " + typeName + ": " + elapsedTime + " ms ");
        printFirstElements();
    }

    private void printFirstElements() {
        for (int i = 0; i < 20; i++) {
            System.out.println(arr[i]);
        }
    }

    protected final T[] getArr() {
        return arr;
    }

    protected abstract T[] createArr(int arrSize);

    protected abstract void fillArrayWithOnes();

    protected abstract void calculateArray(int startPos, int endPos);

}