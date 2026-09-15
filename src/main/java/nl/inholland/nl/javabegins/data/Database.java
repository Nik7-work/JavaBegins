package nl.inholland.nl.javabegins.data;

import nl.inholland.nl.javabegins.model.Manager;
import nl.inholland.nl.javabegins.model.Student;
import nl.inholland.nl.javabegins.model.Teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Student> students = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Manager> managers = new ArrayList<>();

    public Database() {

        students.add(new Student("alice", "pass", "Alice", "Jansen", LocalDate.of(2003, 4, 12), "IN-1A"));
        students.add(new Student("bob", "pass", "Bob", "Pieters", LocalDate.of(2002, 11, 5), "IN-1B"));
        students.add(new Student("clara", "pass", "Clara", "de Vries", LocalDate.of(2004, 1, 20), "IN-1A"));


        teachers.add(new Teacher("david", "pass", "David", "Bakker", LocalDate.of(1980, 6, 15), 4500.0));
        teachers.add(new Teacher("eva", "pass", "Eva", "Smit", LocalDate.of(1975, 9, 3), 5200.0));
        teachers.add(new Teacher("frank", "pass", "Frank", "Meijer", LocalDate.of(1985, 2, 28), 4100.0));


        managers.add(new Manager("grace", "pass", "Grace", "Visser"));
        managers.add(new Manager("henry", "pass", "Henry", "de Boer"));
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Manager> getManagers() {
        return managers;
    }
}
