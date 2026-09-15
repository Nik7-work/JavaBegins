module nl.inholland.nl.javabegins {
    requires javafx.controls;
    requires javafx.fxml;

    opens nl.inholland.nl.javabegins to javafx.fxml;
    opens nl.inholland.nl.javabegins.model to javafx.base;
    opens nl.inholland.nl.javabegins.data to javafx.base;
    opens nl.inholland.nl.javabegins.service to javafx.base;

    exports nl.inholland.nl.javabegins;
}