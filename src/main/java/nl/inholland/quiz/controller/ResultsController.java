
<?xml version="1.0" encoding="UTF-8"?>

        <?import javafx.scene.layout.VBox?>
        <?import javafx.scene.layout.HBox?>
        <?import javafx.scene.control.Label?>
        <?import javafx.scene.control.Button?>

<VBox xmlns:fx="http://javafx.com/fxml"
fx:controller="nl.inholland.quiz.controller.MenuController"
alignment="CENTER"
spacing="18"
styleClass="screen">

    <Label text="JAVA FUNDAMENTALS"
styleClass="eyebrow"/>

    <Label text="Quiz Challenge"
styleClass="hero"/>

    <Label fx:id="titleLabel"
text="No quiz selected"
styleClass="heading"
wrapText="true"/>

    <Label fx:id="infoLabel"
text="Load a JSON quiz to begin."
wrapText="true"/>

    <HBox alignment="CENTER"
spacing="16">

        <Button text="Load quiz"
onAction="#loadQuiz"
styleClass="secondary"/>

        <Button fx:id="startButton"
text="Start quiz"
onAction="#startQuiz"
styleClass="primary"
disable="true"/>

    </HBox>

    <Label fx:id="statusLabel"
text="Choose a JSON file using Load quiz."
wrapText="true"
styleClass="status"/>

</VBox>
