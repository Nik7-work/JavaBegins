
package nl.inholland.quiz.model;

import java.util.List;

public final class BooleanQuestion extends Question {

    private final boolean correctAnswer;
    private final String trueLabel;
    private final String falseLabel;

    public BooleanQuestion(
            String name,
            String title,
            int timeLimit,
            boolean correctAnswer,
            String trueLabel,
            String falseLabel
    ) {

        super(name, title, timeLimit);

        this.correctAnswer = correctAnswer;

        this.trueLabel =
                trueLabel == null || trueLabel.isBlank()
                        ? "True"
                        : trueLabel;

        this.falseLabel =
                falseLabel == null || falseLabel.isBlank()
                        ? "False"
                        : falseLabel;

        if (this.trueLabel.equals(this.falseLabel)) {
            throw new IllegalArgumentException(
                    "True and False labels must differ."
            );
        }
    }

    @Override
    protected boolean matches(String answer) {

        String expectedAnswer = correctAnswer
                ? trueLabel
                : falseLabel;

        return expectedAnswer.equals(answer);
    }

    @Override
    public List<String> getOptions() {
        return List.of(trueLabel, falseLabel);
    }
}
