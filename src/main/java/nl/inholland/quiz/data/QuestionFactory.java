
package nl.inholland.quiz.data;

import com.fasterxml.jackson.databind.JsonNode;

import nl.inholland.quiz.model.BooleanQuestion;
import nl.inholland.quiz.model.ChoiceQuestion;
import nl.inholland.quiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public final class QuestionFactory {

    private QuestionFactory() {
    }

    public static Question create(
            JsonNode node,
            int seconds
    ) {

        if (node == null || !node.isObject()) {
            throw new IllegalArgumentException(
                    "Question must be a JSON object."
            );
        }

        String type = required(node, "type");

        return switch (type) {

            case "radiogroup" ->
                    createChoice(node, seconds);

            case "boolean" ->
                    createBoolean(node, seconds);

            default -> null;
        };
    }

    private static Question createChoice(
            JsonNode node,
            int seconds
    ) {

        JsonNode rawChoices = node.get("choices");

        if (rawChoices == null ||
                !rawChoices.isArray()) {

            throw new IllegalArgumentException(
                    "Choices must be an array."
            );
        }

        List<String> choices = new ArrayList<>();

        for (JsonNode choice : rawChoices) {

            if (!choice.isTextual()) {
                throw new IllegalArgumentException(
                        "Choices must contain strings."
                );
            }

            choices.add(choice.asText());
        }

        return new ChoiceQuestion(
                required(node, "name"),
                required(node, "title"),
                seconds,
                choices,
                required(node, "correctAnswer")
        );
    }

    private static Question createBoolean(
            JsonNode node,
            int seconds
    ) {

        JsonNode answer = node.get("correctAnswer");

        if (answer == null || !answer.isBoolean()) {
            throw new IllegalArgumentException(
                    "Boolean correctAnswer is invalid."
            );
        }

        return new BooleanQuestion(
                required(node, "name"),
                required(node, "title"),
                seconds,
                answer.booleanValue(),
                optional(node, "labelTrue"),
                optional(node, "labelFalse")
        );
    }

    private static String required(
            JsonNode node,
            String key
    ) {

        JsonNode value = node.get(key);

        if (value == null ||
                !value.isTextual() ||
                value.asText().isBlank()) {

            throw new IllegalArgumentException(
                    "Missing or invalid field: " + key
            );
        }

        return value.asText();
    }

    private static String optional(
            JsonNode node,
            String key
    ) {

        JsonNode value = node.get(key);

        if (value != null && value.isTextual()) {
            return value.asText();
        }

        return null;
    }
}
