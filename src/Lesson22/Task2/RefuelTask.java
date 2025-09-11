package Lesson22.Task2;

import java.util.concurrent.CountDownLatch;

public class RefuelTask implements Runnable {
    private final int carId;
    private final double fuelRequested;
    private final PetrolStation station;
    private final PetrolStationLogger logger;
    private final CountDownLatch latch;

    public RefuelTask(int carId, double fuelRequested, PetrolStation station,
                      PetrolStationLogger logger, CountDownLatch latch) {
        this.carId = carId;
        this.fuelRequested = fuelRequested;
        this.station = station;
        this.logger = logger;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            if (fuelRequested < 0) {
                logger.logInvalidFuelRequest(carId);
                return;
            }
            logger.logCarArrival(carId, fuelRequested);
            FuelCheckResult checkResult = station.doTank(fuelRequested);
            if (checkResult.available()) {
                performRefuel(carId, checkResult.fuelToDispense());
            } else {
                handleInsufficientFuel(carId, fuelRequested, checkResult.availableFuel());
            }
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }

    private void handleInsufficientFuel(int carId, double fuelRequested, double fuelAvailable) {
        logger.logInsufficientFuel(carId, fuelRequested, fuelAvailable);
        logger.logRefuelFinishedUnsuccessful(carId);
        logger.logRemainingFuel(station.getFuelAmount());
    }

    private void performRefuel(int carId, double fuelToDispense) {
        int delayMillis = 3000;
        logger.logSufficientFuel(carId, station.getFuelAmount() + fuelToDispense, fuelToDispense);
        logger.logRefuelDelay(carId, delayMillis / 1000.0);
        try {
            Thread.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.logRefuelInterrupted(carId);
            return;
        }
        logger.logCarRefueled(carId, fuelToDispense);
        logger.logRemainingFuel(station.getFuelAmount());
    }

    public record FuelCheckResult(boolean available, double fuelToDispense, double availableFuel) {
    }
}