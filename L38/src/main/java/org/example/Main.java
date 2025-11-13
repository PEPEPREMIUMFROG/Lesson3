package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        System.out.println("--- Adding Students ---");
        Student student1 = new Student("Alice Johnson", "alice@example.com");
        Student student2 = new Student("Bob Smith", "bob@example.com");
        Student student3 = new Student("Charlie Brown", "charlie@example.com");
        studentDAO.save(student1);
        studentDAO.save(student2);
        studentDAO.save(student3);
        System.out.println("Students added.\n");
        System.out.println("--- Fetching All Students ---");
        List<Student> allStudents = studentDAO.findAll();
        if (allStudents != null) {
            for (Student s : allStudents) {
                System.out.println(s);
            }
        } else {
            System.out.println("Failed to fetch students.");
        }
        System.out.println();
        System.out.println("--- Fetching Student by ID (e.g., ID=1) ---");
        Long studentId = 1L;
        Student foundStudent = studentDAO.getById(studentId);
        if (foundStudent != null) {
            System.out.println("Found: " + foundStudent);
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
        System.out.println();
        System.out.println("--- Updating Student (ID=1) ---");
        if (foundStudent != null) {
            foundStudent.setName("Alice Updated");
            foundStudent.setEmail("alice.updated@example.com");
            studentDAO.update(foundStudent);
            System.out.println("Student updated. Fetching again to confirm:");
            Student updatedStudent = studentDAO.getById(studentId);
            System.out.println("Updated: " + updatedStudent);
        } else {
            System.out.println("Cannot update - Student with ID " + studentId + " not found.");
        }
        System.out.println();
        System.out.println("--- Deleting Student (ID=2) ---");
        Long studentIdToDelete = 2L;
        Student studentToDelete = studentDAO.getById(studentIdToDelete);
        if (studentToDelete != null) {
            System.out.println("Deleting: " + studentToDelete);
            studentDAO.delete(studentIdToDelete);
            System.out.println("Student deleted.");
        } else {
            System.out.println("Student with ID " + studentIdToDelete + " not found for deletion.");
        }
        System.out.println();
        System.out.println("--- Fetching All Students After Deletion ---");
        List<Student> remainingStudents = studentDAO.findAll();
        if (remainingStudents != null) {
            for (Student s : remainingStudents) {
                System.out.println(s);
            }
        } else {
            System.out.println("Failed to fetch students.");
        }
        System.out.println();
        HibernateSession.shutdown();
    }
}