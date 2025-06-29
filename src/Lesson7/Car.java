package Lesson7;

public class Car {

    public void start(){
        startElectricity();
        startCommand();
        startFuelSystem();
    }

    private void startElectricity(){
        System.out.println("Electricity was started");
    }
    private void startCommand(){
        System.out.println("Commands was started");
    }
    private void startFuelSystem(){
        System.out.println("Fuel system was started");
    }
}
