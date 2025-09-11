package Lesson22.Task2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RefuelingProcessor {
    private final double initialFuel;
    private final int carCount;
    private final double minFuelPerCar;
    private final double maxFuelPerCar;
    private final int threadsCount;

    public RefuelingProcessor(double initialFuel, int carCount, double minFuelPerCar, double maxFuelPerCar, int threadsCount) {
        this.initialFuel = initialFuel;
        this.carCount = carCount;
        this.minFuelPerCar = minFuelPerCar;
        this.maxFuelPerCar = maxFuelPerCar;
        this.threadsCount = threadsCount;
    }

    public void runRefuelingProcess() {
        PetrolStation ps = new PetrolStation(initialFuel);
        CountDownLatch latch = new CountDownLatch(carCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadsCount);

        System.out.println("___STARTING THE REFUELLING PROCESS\n");

        for (int i = 1; i <= carCount; i++) {
            double fuelRequested = minFuelPerCar + (Math.random() * (maxFuelPerCar - minFuelPerCar));
            System.out.println("[System] Car " + i + " has joined the refueling queue. Requesting " +
                    String.format("%.2f", fuelRequested) + " liters.");
            RefuelTask task = new RefuelTask(i, fuelRequested, ps, ps.getLogger(), latch);
            executor.submit(task);
        }
        try {
            latch.await();
            System.out.println("\n___ALL CARS HAVE BEEN SERVED");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Processor was interrupted while waiting for cars.");
        }
        shutdownExecutor(executor);
        System.out.println("Remaining fuel at the station: "
                + String.format("%.2f", ps.getFuelAmount()) + " liters.");
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    System.err.println("Executor service did not terminate correctly.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}