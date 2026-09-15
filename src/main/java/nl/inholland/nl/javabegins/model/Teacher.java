package nl.inholland.nl.javabegins.model;

import java.time.LocalDate;

public class Teacher extends Person {
    private double salary;

    public Teacher(String username, String password, String firstName, String lastName, LocalDate birthDate, double salary) {
        super(username, password, firstName, lastName, birthDate);
        this.salary = salary;
    }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
