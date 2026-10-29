package nl.inholland.quiz.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import javafx.stage.FileChooser;
import javafx.stage.Modality;

import nl.inholland.quiz.QuizApplication;
import nl.inholland.quiz.data.QuizRepository;
import nl.inholland.quiz.game.GameManager;

import java.io.File;
import java.io.IOException;

public final class MenuController {

    @FXML
    private Button startButton;

    @FXML
    private Label titleLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Label statusLabel;

    private final GameManager game =
            GameManager.getInstance();

    @FXML
    private void initialize() {

        if (game.hasQuiz()) {

            titleLabel.setText(
                    game.getQuiz().getTitle()
            );

            infoLabel.setText(
                    game.getQuiz().getDescription()
            );
        }

        startButton.setDisable(!game.hasQuiz());
    }

    @FXML
    private void loadQuiz() {

        FileChooser picker = new FileChooser();

        picker.setTitle("Select Quiz JSON");

        picker.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "JSON files",
                        "*.json"
                )
        );

        File chosen = picker.showOpenDialog(
                QuizApplication.stage()
        );

        if (chosen == null) {

            statusLabel.setText(
                    "Quiz loading cancelled."
            );

            return;
        }

        try {

            QuizRepository.LoadResult loaded =
                    new QuizRepository().load(
                            chosen.toPath()
                    );

            game.setQuiz(loaded.quiz());

            titleLabel.setText(
                    loaded.quiz().getTitle()
            );

            infoLabel.setText(
                    loaded.quiz().getDescription() +
                            " - " +
                            game.getTotal() +
                            " questions"
            );

            startButton.setDisable(false);

            if (loaded.warnings().isEmpty()) {

                statusLabel.setText(
                        "Quiz loaded successfully."
                );

            } else {

                statusLabel.setText(
                        "Quiz loaded. " +
                                String.join(
                                        " ",
                                        loaded.warnings()
                                )
                );
            }

        } catch (IOException exception) {

            statusLabel.setText(
                    "Cannot load quiz: " +
                            exception.getMessage()
            );
        }
    }

    @FXML
    private void startQuiz() {

        if (!game.hasQuiz()) {

            statusLabel.setText(
                    "Please load a quiz first."
            );

            return;
        }

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.initOwner(
                QuizApplication.stage()
        );

        dialog.initModality(
                Modality.WINDOW_MODAL
        );

        dialog.setTitle("Player Name");

        dialog.setHeaderText(
                "Enter your name to begin."
        );

        dialog.setContentText(
                "Player name:"
        );

        dialog.showAndWait().ifPresent(name -> {

            try {

                game.start(name);

                QuizApplication.navigate(
                        "game-view.fxml"
                );

            } catch (IllegalArgumentException |
                     IllegalStateException exception) {

                statusLabel.setText(
                        exception.getMessage()
                );

            } catch (IOException exception) {

                statusLabel.setText(
                        "Cannot open the game screen."
                );
            }
        });
    }
}
