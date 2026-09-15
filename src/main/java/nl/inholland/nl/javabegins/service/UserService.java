package nl.inholland.nl.javabegins.service;

import nl.inholland.nl.javabegins.data.Database;
import nl.inholland.nl.javabegins.model.*;

public class UserService {
    private final Database db;

    public UserService(Database db) {
        this.db = db;
    }


    public Object login(String username, String password) {
        for (Student s : db.getStudents()) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                return s;
            }
        }
        for (Teacher t : db.getTeachers()) {
            if (t.getUsername().equals(username) && t.getPassword().equals(password)) {
                return t;
            }
        }
        for (Manager m : db.getManagers()) {
            if (m.getUsername().equals(username) && m.getPassword().equals(password)) {
                return m;
            }
        }
        return null; // login failed
    }

    public Role getRole(Object user) {
        if (user instanceof Student) return Role.STUDENT;
        if (user instanceof Teacher) return Role.TEACHER;
        if (user instanceof Manager) return Role.MANAGER;
        return null;
    }

    public boolean canEditStudents(Role role) {
        return role == Role.TEACHER || role == Role.MANAGER;
    }

    public boolean canEditTeachers(Role role) {
        return role == Role.MANAGER;
    }

    public boolean canSeeSalary(Role role) {
        return role == Role.MANAGER;
    }
}
