package nl.inholland.nl.javabegins;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.inholland.nl.javabegins.data.Database;
import nl.inholland.nl.javabegins.model.Role;
import nl.inholland.nl.javabegins.model.Student;
import nl.inholland.nl.javabegins.service.UserService;

import java.time.LocalDate;

public class StudentsController {

    @FXML private TableView<Student> studentTableView;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker birthDatePicker;
    @FXML private TextField groupField;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Label messageLabel;

    private ObservableList<Student> students;
    private UserService userService;
    private Object loggedInUser;
    private Database db;

    private Student studentBeingEdited = null;   // null = add mode, otherwise edit mode

    public void setData(UserService userService, Object loggedInUser, Database db) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
        this.db = db;

        students = FXCollections.observableArrayList(db.getStudents());
        studentTableView.setItems(students);

        Role role = userService.getRole(loggedInUser);
        boolean canEdit = userService.canEditStudents(role);

        addButton.setVisible(canEdit);
        editButton.setVisible(canEdit);
        deleteButton.setVisible(canEdit);

        usernameField.setVisible(canEdit);
        passwordField.setVisible(canEdit);
        firstNameField.setVisible(canEdit);
        lastNameField.setVisible(canEdit);
        birthDatePicker.setVisible(canEdit);
        groupField.setVisible(canEdit);
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        if (studentBeingEdited != null) {
            // We are in Edit mode → save the changes
            saveEditedStudent();
            return;
        }

        // Normal Add mode
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            LocalDate birthDate = birthDatePicker.getValue();
            String group = groupField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
                    lastName.isEmpty() || birthDate == null || group.isEmpty()) {
                showMessage("All fields are required", true);
                return;
            }

            Student student = new Student(username, password, firstName, lastName, birthDate, group);
            students.add(student);
            db.getStudents().add(student);

            clearFields();
            showMessage("Student added successfully", false);
        } catch (Exception e) {
            showMessage("Error adding student", true);
        }
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        Student selected = studentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a student first", true);
            return;
        }

        // Fill the fields
        usernameField.setText(selected.getUsername());
        passwordField.setText(selected.getPassword());
        firstNameField.setText(selected.getFirstName());
        lastNameField.setText(selected.getLastName());
        birthDatePicker.setValue(selected.getBirthDate());
        groupField.setText(selected.getGroup());

        studentBeingEdited = selected;
        addButton.setText("Save");          // change button text
        showMessage("Editing student – click Save when finished", false);
    }

    private void saveEditedStudent() {
        try {
            studentBeingEdited.setUsername(usernameField.getText().trim());
            studentBeingEdited.setPassword(passwordField.getText());
            studentBeingEdited.setFirstName(firstNameField.getText().trim());
            studentBeingEdited.setLastName(lastNameField.getText().trim());
            studentBeingEdited.setBirthDate(birthDatePicker.getValue());
            studentBeingEdited.setGroup(groupField.getText().trim());

            studentTableView.refresh();     // force table to update

            clearFields();
            studentBeingEdited = null;
            addButton.setText("Add");
            showMessage("Student updated successfully", false);
        } catch (Exception e) {
            showMessage("Error updating student", true);
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Student selected = studentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a student first", true);
            return;
        }

        students.remove(selected);
        db.getStudents().remove(selected);
        showMessage("Student deleted", false);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        birthDatePicker.setValue(null);
        groupField.clear();
    }

    private void showMessage(String text, boolean isError) {
        messageLabel.setText(text);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }
}