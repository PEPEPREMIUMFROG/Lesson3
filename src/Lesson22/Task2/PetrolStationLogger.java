package Lesson22.Task2;

import java.io.PrintStream;

public class PetrolStationLogger {

    private final PrintStream outputStream;

    public PetrolStationLogger() {
        this(System.out);
    }

    public PetrolStationLogger(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void logCarArrival(int carId, double fuelRequested) {
        outputStream.println("[Car " + carId + "] arrives at the station, wants to refuel "
                + String.format("%.2f", fuelRequested) + " liters.");
    }

    public void logSufficientFuel(int carId, double fuelAvailable, double fuelRequested) {
        outputStream.println("[Car " + carId + "] Sufficient fuel available ("
                + String.format("%.2f", fuelAvailable) + " liters). Refueling started. Will dispense "
                + String.format("%.2f", fuelRequested) + " liters.");
    }

    public void logInsufficientFuel(int carId, double fuelRequested, double fuelAvailable) {
        outputStream.println("[Car " + carId + "] Insufficient fuel.");
        outputStream.println("  - Requested: " + String.format("%.2f", fuelRequested) + " liters.");
        outputStream.println("  - Available: " + String.format("%.2f", fuelAvailable) + " liters.");
        outputStream.println("  - Shortage: " + String.format("%.2f",
                (fuelRequested - fuelAvailable)) + " liters.");
        outputStream.println("  Refueling will NOT start.");
    }

    public void logRefuelDelay(int carId, double delaySeconds) {
        outputStream.println("[Car " + carId + "] Refueling will take " +
                String.format("%.3f", delaySeconds) + " seconds...");
    }

    public void logCarRefueled(int carId, double fuelDispensed) {
        outputStream.println("[Car " + carId + "] Has been refueled with " +
                String.format("%.2f", fuelDispensed) + " liters of fuel. Pump permit released.");
    }

    public void logRefuelFinishedUnsuccessful(int carId) {
        outputStream.println("[Car " + carId + "] Refueling process finished " +
                "(unsuccessful). Pump permit released.");
    }

    public void logRemainingFuel(double remainingFuel) {
        outputStream.println("  Remaining fuel at station: "
                + String.format("%.2f", remainingFuel) + " liters.");
    }

    public void logRefuelInterrupted(int carId) {
        outputStream.println("[Car " + carId + "] Refueling thread was interrupted.");
    }

    public void logInvalidFuelRequest(int carId) {
        outputStream.println("[Car " + carId + "] Requested fuel amount cannot be negative.");
    }
}