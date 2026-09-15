package nl.inholland.nl.javabegins.model;

import java.time.LocalDate;

public class Student extends Person {
    private String group;

    public Student(String username, String password, String firstName, String lastName, LocalDate birthDate, String group) {
        super(username, password, firstName, lastName, birthDate);
        this.group = group;
    }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
}
