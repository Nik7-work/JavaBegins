# Java Fundamentals — Quiz Game

## 1. How to Run the Application

This project is a JavaFX desktop quiz application developed for the Java Fundamentals Practical Assignment 2026–2027.

The application allows players to load questions from a JSON file, answer questions within a time limit, track their scores and save their results.

### Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA
- Maven
- JavaFX and Jackson dependencies, managed through `pom.xml`

### Running the Application

1. Clone the GitHub repository or open the project folder in IntelliJ IDEA.
2. Configure the project SDK to JDK 25.
3. Allow Maven to download the required dependencies.
4. Open the terminal in the project directory.
5. Run `mvn clean javafx:run`. Alternatively, use `.\mvnw.cmd clean javafx:run` on Windows if the Maven Wrapper is included.
6. The JavaFX Menu screen will appear.
7. Click **Load Quiz** and select a valid JSON quiz file.
8. Click **Start Quiz** and enter a player name.
9. Answer each question before its countdown timer expires.
10. After the final question, the Results screen displays the player's score.

Results are automatically saved inside the `results` directory using the filename `<quizId>-results.json`.

If the results file already exists and has a valid structure, the application appends the new result without deleting previous attempts.

The application supports multiple-choice (`radiogroup`) and true/false (`boolean`) question types.

### Application Features

- JSON quiz loading using Jackson.
- File selection through JavaFX FileChooser.
- Player name validation before starting.
- Multiple-choice and true/false questions.
- Individual countdown timer for each question.
- Progress bar indicating remaining time.
- Automatic score updates using JavaFX property binding.
- Question-by-question navigation.
- Prevention of returning to previously answered questions.
- Final score display.
- JSON result saving with support for multiple attempts.
- Protection against duplicate saving of the same completed attempt during the current application session.
- Error messages for invalid files and unsupported question types.

## 2. Design Rationale

The application uses five design patterns: Model-View-Controller (MVC), Singleton, Observer, Factory and Template Method.

**MVC:** The application separates responsibilities into model classes, FXML views and controllers. The `Quiz`, `Question`, `ChoiceQuestion` and `BooleanQuestion` classes represent quiz data and answer-checking rules. The `menu-view.fxml`, `game-view.fxml` and `results-view.fxml` files define the user interface. The `MenuController`, `GameController` and `ResultsController` classes manage user interactions. This separation keeps the quiz logic independent of the interface and makes the code easier to maintain.

**Singleton and Observer:** The `GameManager` class implements Singleton through a private constructor and a static instance accessed using `getInstance()`. It manages the current quiz, player name, question index and score. The Observer pattern is implemented using JavaFX properties and binding. The score is stored as an `IntegerProperty`, and `GameController` binds the score label to `scoreProperty()`. This allows the displayed score to update automatically whenever the value changes.

**Factory and Template Method:** The `QuestionFactory` class creates `ChoiceQuestion` or `BooleanQuestion` objects according to the question type specified in the JSON file. This centralises question creation and keeps parsing decisions separate from gameplay logic. The abstract `Question` class implements Template Method through its final `checkAnswer()` method. It validates the supplied answer before calling the abstract `matches()` method, which is implemented differently by each question subclass.

The application also includes result-saving protection in `GameManager`. A `resultSaved` field tracks whether the current completed attempt has already been saved. `ResultsController` checks this value before writing to the results file. The flag resets when a new quiz is loaded or a new attempt begins. This prevents duplicate saving during the current application session without affecting the ability to save subsequent attempts.

With more development time, I would consider introducing an explicit game-state model to manage transitions between the Menu, Playing and Results phases. I would also expand automated testing for JSON validation, question handling and results persistence.

## 3. Use of AI

During the development of this Java Fundamentals Quiz Game, I used **ChatGPT, Grok and Google Gemini** as supporting learning and problem-solving tools.

The application's development was primarily guided by the weekly lecture presentations, practical exercises and assignment instructions. These materials provided the basis for learning Java programming, JavaFX, FXML, object-oriented programming, design patterns, JSON handling, exception handling and concurrency.

I used the weekly course materials to develop the application structure and implement its functionality. When I encountered difficulties understanding programming concepts, resolving errors or implementing particular features, I consulted AI tools for explanations, debugging assistance and suggestions.

AI assistance contributed to the development and review of the model classes, controllers, quiz-loading functionality, game management, design patterns and error handling. The course presentations and assignment requirements remained the main references for determining how the application should behave.

AI-assisted suggestions were also used when reviewing the results-saving logic, including the addition of a flag to prevent duplicate saving of a completed quiz attempt.

I used PlantUML to create the UML class diagram. ChatGPT assisted with preparing and reviewing the PlantUML definitions, including class attributes, methods, inheritance and relationships.

AI tools were also used to review the project documentation and compare the implementation against the assignment requirements.

I reviewed the AI-assisted suggestions against the course materials and incorporated them where appropriate. I take responsibility for understanding the submitted code, explaining its design decisions and verifying that the application meets the assignment requirements.

## 4. Known Limitations

The application has the following limitations:

**Supported question types:** The application supports the two question types required for the supplied quiz format: multiple-choice and true/false. Other question types are not implemented.

**Unsupported questions:** When an unknown question type is encountered, the application skips it and displays a warning. If no supported questions remain, the quiz cannot be started.

**JSON structure:** The application expects one question per page, as permitted by the assignment specification. Quiz files using more complex SurveyJS structures are not supported.

**Invalid results files:** If an existing results file contains invalid JSON or an incompatible structure, the application reports an error and avoids overwriting the existing file. The user must resolve the file issue before the result can be saved successfully.

**Results persistence:** The application tracks whether the current quiz attempt has already been saved. This prevents the same completed attempt from being saved repeatedly during the current application session. A new attempt resets the saving status. The application does not provide persistent duplicate detection across separate application sessions.

**Countdown timer:** The application uses JavaFX `Timeline` to manage the countdown for each question. This is an accepted JavaFX mechanism under the assignment requirements and keeps countdown updates on the JavaFX application thread.

**Testing:** Automated unit tests have not yet been added. Further verification of malformed JSON files, unexpected fields, application resizing and fresh-clone compatibility is recommended before final submission.
