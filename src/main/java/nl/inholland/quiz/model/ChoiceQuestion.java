
package nl.inholland.quiz.model;

import java.util.List;

public final class ChoiceQuestion extends Question {

    private final List<String> choices;
    private final String correctAnswer;

    public ChoiceQuestion(
            String name,
            String title,
            int timeLimit,
            List<String> choices,
            String correctAnswer
    ) {

        super(name, title, timeLimit);

        if (choices == null || choices.size() < 2) {
            throw new IllegalArgumentException(
                    "At least two choices are required."
            );
        }

        if (choices.stream().anyMatch(
                choice -> choice == null || choice.isBlank()
        )) {
            throw new IllegalArgumentException(
                    "Choices cannot be empty."
            );
        }

        if (!choices.contains(correctAnswer)) {
            throw new IllegalArgumentException(
                    "Correct answer must be one of the choices."
            );
        }

        this.choices = List.copyOf(choices);
        this.correctAnswer = correctAnswer;
    }

    @Override
    protected boolean matches(String answer) {
        return correctAnswer.equals(answer);
    }

    @Override
    public List<String> getOptions() {
        return choices;
    }
}
