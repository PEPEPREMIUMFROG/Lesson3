package Lesson21;

public class Main {
    private static final int ARR_SIZE = 10_000_000;
    public static void main(String[] args){
        System.out.println("Starting calculations:");
        long start = System.nanoTime();
        DoubleValueCalculator dvc = new DoubleValueCalculator(ARR_SIZE);
        FloatValueCalculator fvc = new FloatValueCalculator(ARR_SIZE);
        dvc.doCalc();
        fvc.doCalc();
        long end = System.nanoTime();
        long totalTime = (end - start) / 1_000_000;
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("All calculations completed!");
    }
}
