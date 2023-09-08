module com.java.project.tournamenttracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;

    opens com.java.project.tournamenttracker.main to javafx.fxml;
    exports com.java.project.tournamenttracker.main;
}