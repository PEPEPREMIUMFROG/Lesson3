package Lesson9;

import net.datafaker.Faker;

import java.util.Random;

public class Main {
    private static final int NUMBER_OF_EMPLOYEES = 5;

    public static void main(String[] args) {
        Random random = new Random();
        Faker faker = new Faker();

        Employee[] employees = new Employee[NUMBER_OF_EMPLOYEES];
        for (int i = 0; i <= employees.length-1; i++) {
            employees[i] = new Employee(faker.name().firstName(), faker.name().lastName(), faker.name().firstName(),
                    faker.job().position(), faker.internet().emailAddress(), faker.phoneNumber().phoneNumber(),
                    random.nextInt(18, 70), random.nextInt(75000, 100000));
        }

        for (Employee employee : employees) {
            if (employee.getAge() > 40) {
                employee.printInfo(System.out);
            }
        }
    }

}
