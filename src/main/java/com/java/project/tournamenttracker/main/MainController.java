package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.Changes;
import com.java.project.tournamenttracker.threads.LoggedUserThread;
import com.java.project.tournamenttracker.threads.YoungestPlayerThread;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.java.project.tournamenttracker.files.FileAccess.deserializeChanges;
import static com.java.project.tournamenttracker.files.FileAccess.serializeChanges;

public class MainController extends Application {

    private static Stage MainStage;
    @Override
    public void start(Stage stage) throws IOException {
        MainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.getIcons().add(new Image(new FileInputStream("dat\\img\\dpc_logo.png")));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        Timeline threads = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new LoggedUserThread());
            }
        }));
        threads.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.runLater(new YoungestPlayerThread());
            }
        }));
        threads.setCycleCount(Timeline.INDEFINITE);
        threads.play();
        launch();
    }
    public static Stage getStage() {
        return MainStage;
    }
}