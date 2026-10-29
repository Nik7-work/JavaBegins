# Java Fundamentals — Quiz Game

## 1. How to Run the Application

This project is a JavaFX desktop quiz application developed for the Java Fundamentals Practical Assignment 2026–2027.

The application allows users to load quiz questions from a JSON file, answer questions within a time limit, track their scores and save their results.

### Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA
- Maven
- JavaFX and Jackson dependencies, managed through `pom.xml`

### Running the Application

1. Clone the GitHub repository or open the project folder in IntelliJ IDEA.
2. Configure the project to use JDK 25.
3. Allow Maven to download and resolve the dependencies.
4. Open the terminal in the project directory.
5. Run `mvn clean javafx:run`. If using the included Maven Wrapper on Windows, run `.\mvnw.cmd clean javafx:run`.
6. The JavaFX Menu screen will appear.
7. Select **Load Quiz** and choose a valid quiz JSON file.
8. Click **Start Quiz** and enter a player name.
9. Answer the questions before their individual timers expire.
10. After the final question, the Results screen displays the player's score.

Results are automatically saved inside the `results` directory using the filename `<quizId>-results.json`. Existing results are preserved, and new results are appended.

The application supports multiple-choice (`radiogroup`) and true/false (`boolean`) question types.

## 2. Design Rationale

The application uses five design patterns: Model-View-Controller (MVC), Singleton, Observer, Factory and Template Method.

**MVC:** The application separates its responsibilities into models, FXML views and controllers. The `Quiz`, `Question`, `ChoiceQuestion` and `BooleanQuestion` classes represent the data and business rules. The `menu-view.fxml`, `game-view.fxml` and `results-view.fxml` files define the user interface. The `MenuController`, `GameController` and `ResultsController` classes handle user interaction. This separation keeps business logic independent of the interface and makes the application easier to maintain.

**Singleton and Observer:** The `GameManager` class implements the Singleton pattern using a private constructor and a static instance accessible through `getInstance()`. It stores the current quiz, player name, question index and score, allowing the screens to share consistent game information. The Observer pattern is implemented using JavaFX properties and binding. `GameManager` exposes the score through `scoreProperty()`, while `GameController` binds the score label to this property. Consequently, the displayed score changes automatically whenever the underlying score changes.

**Factory and Template Method:** The `QuestionFactory` class creates the appropriate question subclass based on the question type in the JSON file. This centralises object creation and prevents JSON parsing decisions from being scattered throughout the application. The abstract `Question` class implements the Template Method pattern through its final `checkAnswer()` method. This method validates the supplied answer before calling the abstract `matches()` method. The `ChoiceQuestion` and `BooleanQuestion` subclasses provide their own matching logic while sharing the same answer-checking procedure.

With more development time, I would consider introducing an explicit game-state model to manage transitions between the Menu, Playing and Results phases. I would also expand automated testing for JSON validation, question handling and results persistence.

## 3. Use of AI

During the development of this Java Fundamentals Quiz Game, I used ChatGPT, Grok and Google Gemini as supporting learning and problem-solving tools.

The development of the application was primarily based on the weekly lecture presentations, practical exercises and assignment instructions provided throughout the course. These materials guided my understanding of Java programming, JavaFX, FXML, object-oriented programming, design patterns, JSON handling, exception handling and concurrency.

I followed the weekly course materials to develop the application's structure, implement its functions and connect the different components. Whenever I encountered difficulties understanding concepts, resolving programming errors or implementing particular functionality, I consulted AI tools for explanations, debugging assistance and suggestions for possible solutions.

AI assistance was used during the development and review of different parts of the application, including the model classes, controllers, quiz-loading functionality, game management, design patterns and error handling. The weekly presentations and assignment requirements remained the main references for deciding how the application should function.

For the UML class diagram, I used PlantUML to generate the diagram from the application's Java class structure. ChatGPT assisted with preparing and reviewing the PlantUML definitions, including class attributes, methods, inheritance and relationships.

AI tools were also used to review the project documentation and check the implementation against the assignment requirements.

I reviewed the suggestions provided by these tools against the course materials and incorporated them into the project where appropriate. I take responsibility for understanding the submitted code, explaining its design decisions and verifying that the application meets the assignment requirements.
## 4. Known Limitations

The application supports the two question types required for the supplied quiz format: multiple-choice and true/false.

Unsupported question types are skipped during quiz loading, with a warning. If no supported questions remain, the quiz cannot be started.

The application expects one question per page, as permitted by the assignment specification.

If an existing results file contains invalid JSON or an incompatible structure, the application reports the problem rather than overwriting the existing results.

The application currently uses a JavaFX `Timeline` for the question countdown rather than a separate background worker.

Additional limitations identified during testing: [Record any remaining problems or untested behaviour here before submission.]