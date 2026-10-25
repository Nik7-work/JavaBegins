
package nl.inholland.quiz.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import nl.inholland.quiz.model.Question;
import nl.inholland.quiz.model.Quiz;

public final class GameManager {

    private static final GameManager INSTANCE =
            new GameManager();

    private Quiz quiz;
    private String playerName;

    private int index;
    private boolean finished;

    private final IntegerProperty score =
            new SimpleIntegerProperty(0);

    private GameManager() {
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public void setQuiz(Quiz quiz) {

        this.quiz = quiz;
        this.playerName = null;

        this.index = 0;
        this.finished = false;

        score.set(0);
    }

    public boolean hasQuiz() {
        return quiz != null;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void start(String name) {

        if (quiz == null) {
            throw new IllegalStateException(
                    "Load a quiz before starting."
            );
        }

        if (name == null ||
                name.isBlank() ||
                name.trim().length() > 60) {

            throw new IllegalArgumentException(
                    "Enter a name between 1 and 60 characters."
            );
        }

        playerName = name.trim();

        index = 0;
        finished = false;

        score.set(0);
    }

    public Question current() {

        if (finished ||
                quiz == null ||
                index >= quiz.getQuestions().size()) {

            return null;
        }

        return quiz.getQuestions().get(index);
    }

    public boolean answer(String selected) {

        Question question = current();

        if (question == null) {
            throw new IllegalStateException(
                    "There is no active question."
            );
        }

        boolean correct =
                question.checkAnswer(selected);

        if (correct) {
            score.set(score.get() + 1);
        }

        index++;

        finished =
                index >= quiz.getQuestions().size();

        return correct;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getScore() {
        return score.get();
    }

    public ReadOnlyIntegerProperty scoreProperty() {
        return score;
    }

    public int getQuestionNumber() {
        return index + 1;
    }

    public int getTotal() {

        if (quiz == null) {
            return 0;
        }

        return quiz.getQuestions().size();
    }
}
