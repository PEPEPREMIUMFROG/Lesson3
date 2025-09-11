package Lesson22.Task2;

public class Main {
    private static final int CAR_COUNT = 7;
    private static final int THREAD_COUNT = 3;
    private static final double INITIAL_FUEL = 300.0;
    private static final double MIN_FUEL_IN_CAR = 60.0;
    private static final double MAX_FUEL_IN_CAR = 60.0;

    public static void main(String[] args) {
        RefuelingProcessor processor = new RefuelingProcessor(INITIAL_FUEL, CAR_COUNT,
                MIN_FUEL_IN_CAR, MAX_FUEL_IN_CAR, THREAD_COUNT
        );
        processor.runRefuelingProcess();
    }
}