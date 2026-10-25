
package nl.inholland.quiz.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.inholland.quiz.model.Quiz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

public final class ResultsRepository {

    private final ObjectMapper mapper =
            new ObjectMapper();

    public Path save(
            Quiz quiz,
            String player,
            int correct,
            Path folder
    ) throws IOException {

        Path destination = folder.resolve(
                quiz.getId() + "-results.json"
        );

        ObjectNode output;

        if (Files.exists(destination)) {

            if (!Files.isRegularFile(destination) ||
                    !Files.isReadable(destination)) {

                throw new IOException(
                        "Existing results file is unreadable."
                );
            }

            JsonNode current;

            try {

                current = mapper.readTree(
                        destination.toFile()
                );

            } catch (IOException exception) {

                throw new IOException(
                        "Existing results JSON is corrupt. " +
                                "The file was not overwritten.",
                        exception
                );
            }

            if (current == null ||
                    !current.isObject() ||
                    !quiz.getId().equals(
                            current.path("quizId").asText()
                    ) ||
                    !current.path("results").isArray()) {

                throw new IOException(
                        "Existing results file has " +
                                "an incompatible format."
                );
            }

            output = (ObjectNode) current;

        } else {

            output = mapper.createObjectNode();

            output.put("quizId", quiz.getId());
            output.put("name", quiz.getTitle());

            output.set(
                    "results",
                    mapper.createArrayNode()
            );
        }

        ArrayNode results =
                (ArrayNode) output.get("results");

        ObjectNode entry = results.addObject();

        entry.put("playerName", player);

        entry.put(
                "totalQuestions",
                quiz.getQuestions().size()
        );

        entry.put("correctQuestions", correct);

        entry.put(
                "date",
                Instant.now().toString()
        );

        Files.createDirectories(folder);

        Path temporary = Files.createTempFile(
                folder,
                "quiz-results-",
                ".tmp"
        );

        try {

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            temporary.toFile(),
                            output
                    );

            Files.move(
                    temporary,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } finally {

            Files.deleteIfExists(temporary);
        }

        return destination;
    }
}
