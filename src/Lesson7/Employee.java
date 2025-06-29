package Lesson7;

public class Employee {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String position;
    private String email;
    private String phoneNumber;
    private int age;

    public Employee(String firstName, String lastName, String patronymic, String position, String email, String phoneNumber, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.position = position;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }
}
