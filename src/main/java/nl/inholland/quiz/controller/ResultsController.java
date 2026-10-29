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

        if (!game.isFinished() ||
                game.getQuiz() == null ||
                game.getPlayerName() == null) {

            summaryLabel.setText(
                    "No completed quiz result is available."
            );

            statusLabel.setText(
                    "Please complete a quiz first."
            );

            return;
        }

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

        if (game.isResultSaved()) {

            statusLabel.setText(
                    "This quiz result has already been saved."
            );

            return;
        }

        try {

            ResultsRepository repository =
                    new ResultsRepository();

            Path destination = repository.save(
                    game.getQuiz(),
                    game.getPlayerName(),
                    game.getScore(),
                    Path.of("results")
            );

            // Mark as saved only after a successful write.
            game.markResultSaved();

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
