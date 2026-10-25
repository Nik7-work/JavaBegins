
package nl.inholland.quiz.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import nl.inholland.quiz.QuizApplication;
import nl.inholland.quiz.data.ResultsRepository;
import nl.inholland.quiz.game.GameManager;

import java.io.IOException;
import java.nio.file.Path;

public final class ResultsController {

    @FXML
    private Label summaryLabel;

    @FXML
    private Label statusLabel;

    private final GameManager game =
            GameManager.getInstance();

    @FXML
    private void initialize() {

        summaryLabel.setText(
                game.getPlayerName() +
                        ", you scored " +
                        game.getScore() +
                        " out of " +
                        game.getTotal() +
                        " in " +
                        game.getQuiz().getTitle()
        );

        saveResult();
    }

    private void saveResult() {

        try {
            ResultsRepository repository =
                    new ResultsRepository();

            Path destination = repository.save(
                    game.getQuiz(),
                    game.getPlayerName(),
                    game.getScore(),
                    Path.of("results")
            );

            statusLabel.setText(
                    "Result saved: " +
                            destination.toAbsolutePath()
            );

        } catch (IOException exception) {

            statusLabel.setText(
                    "Result could not be saved: " +
                            exception.getMessage()
            );
        }
    }

    @FXML
    private void backToMenu() {

        try {
            QuizApplication.navigate(
                    "menu-view.fxml"
            );

        } catch (IOException exception) {

            statusLabel.setText(
                    "Could not return to menu."
            );
        }
    }
}
