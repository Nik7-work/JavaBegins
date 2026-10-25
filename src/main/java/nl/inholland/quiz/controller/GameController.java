
package nl.inholland.quiz.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.VBox;

import javafx.util.Duration;

import nl.inholland.quiz.QuizApplication;
import nl.inholland.quiz.game.GameManager;
import nl.inholland.quiz.model.Question;

import java.io.IOException;

public final class GameController {

    @FXML
    private Label headingLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label countdownLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox choicesBox;

    @FXML
    private ProgressBar progressBar;

    private final GameManager game =
            GameManager.getInstance();

    private Timeline timeline;

    private int remaining;

    private boolean submitting;

    private ToggleGroup group;

    @FXML
    private void initialize() {

        scoreLabel.textProperty().bind(
                game.scoreProperty()
                        .asString("Score: %d")
        );

        showQuestion();
    }

    private void showQuestion() {

        Question question = game.current();

        if (question == null) {
            finish();
            return;
        }

        submitting = false;

        headingLabel.setText(
                "Question " +
                        game.getQuestionNumber() +
                        " of " +
                        game.getTotal()
        );

        questionLabel.setText(
                question.getTitle()
        );

        choicesBox.getChildren().clear();

        group = new ToggleGroup();

        for (String option : question.getOptions()) {

            RadioButton radio =
                    new RadioButton(option);

            radio.setToggleGroup(group);

            radio.setUserData(option);

            radio.getStyleClass().add(
                    "answer-choice"
            );

            radio.setWrapText(true);

            radio.setMaxWidth(
                    Double.MAX_VALUE
            );

            choicesBox.getChildren().add(radio);
        }

        remaining = question.getTimeLimit();

        showTime();

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {

                            remaining--;

                            showTime();

                            if (remaining <= 0) {

                                timeline.stop();

                                submit(null);
                            }
                        }
                )
        );

        timeline.setCycleCount(
                Timeline.INDEFINITE
        );

        timeline.play();

        statusLabel.setText(
                "Select an answer before time expires."
        );
    }

    private void showTime() {

        countdownLabel.setText(
                remaining + " seconds remaining"
        );

        Question question = game.current();

        if (question == null) {

            progressBar.setProgress(0);

            return;
        }

        double progress =
                (double) remaining /
                        question.getTimeLimit();

        progressBar.setProgress(progress);
    }

    @FXML
    private void submitAnswer() {

        if (submitting || group == null) {
            return;
        }

        Toggle selected =
                group.getSelectedToggle();

        if (selected == null) {

            statusLabel.setText(
                    "Please select an answer."
            );

            return;
        }

        submit(
                (String) selected.getUserData()
        );
    }

    private void submit(String selection) {

        if (submitting) {
            return;
        }

        submitting = true;

        if (timeline != null) {
            timeline.stop();
        }

        game.answer(selection);

        if (game.isFinished()) {

            finish();

        } else {

            showQuestion();
        }
    }

    private void finish() {

        if (timeline != null) {
            timeline.stop();
        }

        try {

            QuizApplication.navigate(
                    "results-view.fxml"
            );

        } catch (IOException exception) {

            statusLabel.setText(
                    "Could not display results."
            );
        }
    }
}
