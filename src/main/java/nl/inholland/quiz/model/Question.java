
package nl.inholland.quiz.model;

import java.util.List;

public abstract class Question {

    private final String name;
    private final String title;
    private final int timeLimit;

    protected Question(
            String name,
            String title,
            int timeLimit
    ) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Question name is required."
            );
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Question title is required."
            );
        }

        if (timeLimit <= 0) {
            throw new IllegalArgumentException(
                    "Time limit must be positive."
            );
        }

        this.name = name;
        this.title = title;
        this.timeLimit = timeLimit;
    }

    public final String getName() {
        return name;
    }

    public final String getTitle() {
        return title;
    }

    public final int getTimeLimit() {
        return timeLimit;
    }

    // Template Method pattern
    public final boolean checkAnswer(String answer) {

        if (answer == null || answer.isBlank()) {
            return false;
        }

        return matches(answer.trim());
    }

    protected abstract boolean matches(String answer);

    public abstract List<String> getOptions();
}
