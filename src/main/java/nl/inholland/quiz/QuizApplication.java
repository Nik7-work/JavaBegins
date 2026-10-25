
package nl.inholland.quiz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class QuizApplication extends Application {

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {

        stage = primaryStage;

        stage.setTitle("Java Fundamentals Quiz");
        stage.setMinWidth(620);
        stage.setMinHeight(520);

        navigate("menu-view.fxml");

        stage.show();
    }

    public static void navigate(String view) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                QuizApplication.class.getResource(view)
        );

        Parent root = loader.load();

        double width = 820;
        double height = 600;

        if (stage.getScene() != null) {
            width = stage.getScene().getWidth();
            height = stage.getScene().getHeight();
        }

        Scene scene = new Scene(root, width, height);

        scene.getStylesheets().add(
                QuizApplication.class
                        .getResource("style.css")
                        .toExternalForm()
        );

        stage.setScene(scene);
    }

    public static Stage stage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
