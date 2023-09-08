package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.exceptions.LoginException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.java.project.tournamenttracker.files.FileAccess.*;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    public static User loggedUser;
    public static User getLoggedUser(){
        return loggedUser;
    }
    public static void setLoggedUser(){
        loggedUser = null;
    }
    @FXML
    public void initialize() throws FileNotFoundException {
        gridPane.setAlignment(Pos.CENTER);
        Image image = new Image(new FileInputStream("dat\\img\\background.jpg"));
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        gridPane.setBackground(background);
    }
    @FXML
    protected void onLoginClick(){
        StringBuilder errorMessages = new StringBuilder();

        String username = usernameTextField.getText();
        if (username.isEmpty()){
            errorMessages.append("Username must be entered!\n");
        }
        String pass = passwordTextField.getText();
        if (pass.isEmpty()){
            errorMessages.append("Password must be entered!\n");
        }
        try{
            loggedUser = readUsers().stream()
                    .filter(u -> u.getUsername().toLowerCase().equals(username.toLowerCase()) && u.getPassword().equals(u.hashPassword(pass)))
                    .findFirst()
                    .orElse(null);
            if (loggedUser != null){
                FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("homeScreen.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 600, 700);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainController.getStage().setTitle("Home");
                MainController.getStage().setScene(scene);
                MainController.getStage().show();
            }
            else{
                if (!username.equals("") || !pass.equals("")){
                    errorMessages.append("Incorrect username and/or password!\n");
                    errorMessages.append("Please enter credentials again!\n");
                    throw new LoginException("Login error, incorrect credentials!");
                }
            }
        }catch (LoginException ex){
            logger.error(ex.getMessage(), ex);
        }
    if (!errorMessages.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login error!");
        alert.setHeaderText("Login failed!");
        alert.setContentText(errorMessages.toString());
        alert.showAndWait();
    }
    }
    @FXML
    protected void onGuestClick(){
        String username = "guest";
        String pass = "guest";
        loggedUser = readUsers().stream()
                .filter(u -> u.getUsername().toLowerCase().equals(username.toLowerCase()) && u.getPassword().equals(u.hashPassword(pass)))
                .findFirst()
                .orElse(null);
        if (loggedUser != null){
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("homeScreen.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 600, 700);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainController.getStage().setTitle("Home");
            MainController.getStage().setScene(scene);
            MainController.getStage().show();
        }
    }
}
