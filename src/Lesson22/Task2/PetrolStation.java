package Lesson22.Task2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PetrolStation {
    private double fuelAmount;
    private final Lock fuelLock = new ReentrantLock();
    private final PetrolStationLogger logger;

    public PetrolStation(double initialFuel) {
        this(initialFuel, new PetrolStationLogger());
    }

    public PetrolStation(double initialFuel, PetrolStationLogger logger) {
        if (initialFuel < 0) {
            throw new IllegalArgumentException("Initial fuel amount cannot be negative.");
        }
        this.fuelAmount = initialFuel;
        this.logger = logger;
    }

    public double getFuelAmount() {
        fuelLock.lock();
        try {
            return fuelAmount;
        } finally {
            fuelLock.unlock();
        }
    }

    public RefuelTask.FuelCheckResult doTank (double fuelNeeded) {
        fuelLock.lock();
        try {
            double availableFuel = this.fuelAmount;
            if (availableFuel >= fuelNeeded) {
                this.fuelAmount -= fuelNeeded;
                return new RefuelTask.FuelCheckResult(true, fuelNeeded, availableFuel);
            } else {
                return new RefuelTask.FuelCheckResult(false, 0.0, availableFuel);
            }
        } finally {
            fuelLock.unlock();
        }
    }

    public PetrolStationLogger getLogger() {
        return logger;
    }

}