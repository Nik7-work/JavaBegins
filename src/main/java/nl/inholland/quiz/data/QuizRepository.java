package nl.inholland.quiz.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.inholland.quiz.model.Question;
import nl.inholland.quiz.model.Quiz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QuizRepository {

    private final ObjectMapper mapper =
            new ObjectMapper();

    public record LoadResult(
            Quiz quiz,
            List<String> warnings
    ) {
    }

    public LoadResult load(Path file) throws IOException {

        if (!Files.isRegularFile(file) ||
                !Files.isReadable(file)) {

            throw new IOException(
                    "The selected file cannot be read."
            );
        }

        if (Files.size(file) == 0) {
            throw new IOException(
                    "The quiz file is empty."
            );
        }

        JsonNode root;

        try {
            root = mapper.readTree(file.toFile());
        } catch (IOException exception) {
            throw new IOException(
                    "The selected file is not valid JSON.",
                    exception
            );
        }

        if (root == null || !root.isObject()) {
            throw new IOException(
                    "The file does not contain a quiz object."
            );
        }

        String id = required(root, "quizId");
        String title = required(root, "title");

        JsonNode pages = root.get("pages");

        if (pages == null ||
                !pages.isArray() ||
                pages.isEmpty()) {

            throw new IOException(
                    "The quiz does not contain valid pages."
            );
        }

        List<Question> questions = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        for (int i = 0; i < pages.size(); i++) {

            JsonNode page = pages.get(i);

            if (page == null || !page.isObject()) {
                throw new IOException(
                        "Page " + (i + 1) + " is invalid."
                );
            }

            JsonNode time = page.get("timeLimit");

            if (time == null ||
                    !time.canConvertToInt() ||
                    time.intValue() <= 0) {

                throw new IOException(
                        "Page " + (i + 1) +
                                " requires a positive time limit."
                );
            }

            JsonNode elements = page.get("elements");

            if (elements == null ||
                    !elements.isArray() ||
                    elements.size() != 1) {

                throw new IOException(
                        "Page " + (i + 1) +
                                " must contain one question."
                );
            }

            try {

                Question question = QuestionFactory.create(
                        elements.get(0),
                        time.intValue()
                );

                if (question == null) {

                    warnings.add(
                            "Skipped unsupported question " +
                                    "type on page " + (i + 1) + "."
                    );

                } else {

                    questions.add(question);
                }

            } catch (IllegalArgumentException exception) {

                throw new IOException(
                        "Page " + (i + 1) + ": " +
                                exception.getMessage(),
                        exception
                );
            }
        }

        try {

            Quiz quiz = new Quiz(
                    id,
                    title,
                    root.path("description").asText(""),
                    questions
            );

            return new LoadResult(
                    quiz,
                    List.copyOf(warnings)
            );

        } catch (IllegalArgumentException exception) {

            throw new IOException(
                    "Invalid quiz: " + exception.getMessage(),
                    exception
            );
        }
    }

    private static String required(
            JsonNode root,
            String key
    ) throws IOException {

        JsonNode value = root.get(key);

        if (value == null ||
                !value.isTextual() ||
                value.asText().isBlank()) {

            throw new IOException(
                    "Missing or invalid quiz field: " + key
            );
        }

        return value.asText();
    }
}
