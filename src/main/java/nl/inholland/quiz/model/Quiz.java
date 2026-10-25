
package nl.inholland.quiz.model;

import java.util.List;

public final class Quiz {

    private final String id;
    private final String title;
    private final String description;
    private final List<Question> questions;

    public Quiz(
            String id,
            String title,
            String description,
            List<Question> questions
    ) {

        if (id == null ||
                !id.matches("[a-zA-Z0-9_-]{1,60}")) {

            throw new IllegalArgumentException(
                    "Quiz ID is invalid."
            );
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Quiz title is required."
            );
        }

        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException(
                    "Quiz must contain supported questions."
            );
        }

        this.id = id;
        this.title = title;

        this.description =
                description == null ? "" : description;

        this.questions = List.copyOf(questions);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
