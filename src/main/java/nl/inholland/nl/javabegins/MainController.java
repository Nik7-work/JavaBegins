package nl.inholland.nl.javabegins;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nl.inholland.nl.javabegins.data.Database;
import nl.inholland.nl.javabegins.model.Manager;
import nl.inholland.nl.javabegins.model.Student;
import nl.inholland.nl.javabegins.model.Teacher;
import nl.inholland.nl.javabegins.service.UserService;

import java.io.IOException;

public class MainController {

    @FXML private Label welcomeLabel;

    private Object loggedInUser;
    private UserService userService;
    private Database db;

    public void setLoggedInUser(Object user) {
        this.loggedInUser = user;

        String name;
        if (user instanceof Student s) {
            name = s.getFirstName() + " " + s.getLastName() + " (Student)";
        } else if (user instanceof Teacher t) {
            name = t.getFirstName() + " " + t.getLastName() + " (Teacher)";
        } else if (user instanceof Manager m) {
            name = m.getFirstName() + " " + m.getLastName() + " (Manager)";
        } else {
            name = "Unknown";
        }
        welcomeLabel.setText("Welcome, " + name);
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    @FXML
    private void onStudentsButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("students-view.fxml"));
            Parent root = loader.load();

            StudentsController controller = loader.getController();
            controller.setData(userService, loggedInUser, db);

            Stage stage = new Stage();
            stage.setTitle("Students");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTeachersButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teachers-view.fxml"));
            Parent root = loader.load();

            TeachersController controller = loader.getController();
            controller.setData(userService, loggedInUser, db);

            Stage stage = new Stage();
            stage.setTitle("Teachers");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}