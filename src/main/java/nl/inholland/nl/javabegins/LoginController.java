package nl.inholland.nl.javabegins;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.inholland.nl.javabegins.data.Database;
import nl.inholland.nl.javabegins.service.UserService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final Database db = new Database();
    private final UserService userService = new UserService(db);

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        Object user = userService.login(username, password);

        if (user == null) {
            messageLabel.setText("Invalid username or password");
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            try {
                openMainWindow(user);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                messageLabel.setText("Error opening main window");
                e.printStackTrace();
            }
        }
    }

    private void openMainWindow(Object user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.setLoggedInUser(user);
        controller.setUserService(userService);
        controller.setDatabase(db);

        Stage stage = new Stage();
        stage.setTitle("University Administration");
        stage.setScene(new Scene(root, 500, 350));
        stage.show();
    }
}