package nl.inholland.nl.javabegins;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.inholland.nl.javabegins.data.Database;
import nl.inholland.nl.javabegins.model.Role;
import nl.inholland.nl.javabegins.model.Teacher;
import nl.inholland.nl.javabegins.service.UserService;

import java.time.LocalDate;

public class TeachersController {

    @FXML private TableView<Teacher> teacherTableView;
    @FXML private TableColumn<Teacher, Double> salaryColumn;

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField salaryField;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Label messageLabel;

    private ObservableList<Teacher> teachers;
    private UserService userService;
    private Object loggedInUser;
    private Database db;

    private Teacher teacherBeingEdited = null;

    public void setData(UserService userService, Object loggedInUser, Database db) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
        this.db = db;

        teachers = FXCollections.observableArrayList(db.getTeachers());
        teacherTableView.setItems(teachers);

        Role role = userService.getRole(loggedInUser);

        boolean canSeeSalary = userService.canSeeSalary(role);
        salaryColumn.setVisible(canSeeSalary);


        boolean canEdit = userService.canEditTeachers(role);
        addButton.setVisible(canEdit);
        editButton.setVisible(canEdit);
        deleteButton.setVisible(canEdit);

        usernameField.setVisible(canEdit);
        passwordField.setVisible(canEdit);
        firstNameField.setVisible(canEdit);
        lastNameField.setVisible(canEdit);
        birthDatePicker.setVisible(canEdit);
        salaryField.setVisible(canEdit);
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        if (teacherBeingEdited != null) {
            saveEditedTeacher();
            return;
        }

        // Normal Add
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            LocalDate birthDate = birthDatePicker.getValue();
            String salaryText = salaryField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
                    lastName.isEmpty() || birthDate == null || salaryText.isEmpty()) {
                showMessage("All fields are required", true);
                return;
            }

            double salary = Double.parseDouble(salaryText);
            Teacher teacher = new Teacher(username, password, firstName, lastName, birthDate, salary);

            teachers.add(teacher);
            db.getTeachers().add(teacher);

            clearFields();
            showMessage("Teacher added successfully", false);
        } catch (NumberFormatException e) {
            showMessage("Salary must be a number", true);
        } catch (Exception e) {
            showMessage("Error adding teacher", true);
        }
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        Teacher selected = teacherTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a teacher first", true);
            return;
        }

        usernameField.setText(selected.getUsername());
        passwordField.setText(selected.getPassword());
        firstNameField.setText(selected.getFirstName());
        lastNameField.setText(selected.getLastName());
        birthDatePicker.setValue(selected.getBirthDate());
        salaryField.setText(String.valueOf(selected.getSalary()));

        teacherBeingEdited = selected;
        addButton.setText("Save");
        showMessage("Editing teacher – click Save when finished", false);
    }

    private void saveEditedTeacher() {
        try {
            teacherBeingEdited.setUsername(usernameField.getText().trim());
            teacherBeingEdited.setPassword(passwordField.getText());
            teacherBeingEdited.setFirstName(firstNameField.getText().trim());
            teacherBeingEdited.setLastName(lastNameField.getText().trim());
            teacherBeingEdited.setBirthDate(birthDatePicker.getValue());
            teacherBeingEdited.setSalary(Double.parseDouble(salaryField.getText().trim()));

            teacherTableView.refresh();

            clearFields();
            teacherBeingEdited = null;
            addButton.setText("Add");
            showMessage("Teacher updated successfully", false);
        } catch (NumberFormatException e) {
            showMessage("Salary must be a number", true);
        } catch (Exception e) {
            showMessage("Error updating teacher", true);
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Teacher selected = teacherTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a teacher first", true);
            return;
        }

        teachers.remove(selected);
        db.getTeachers().remove(selected);
        showMessage("Teacher deleted", false);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.setValue(null);
        salaryField.clear();
    }

    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}