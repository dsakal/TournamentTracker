package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;

public class MenuController {
    @FXML
    private MenuItem tournaments;
    @FXML
    private MenuItem teams;
    @FXML
    private MenuItem players;
    @FXML
    private Menu history;
    private User user = LoginController.getLoggedUser();
    @FXML
    public void initialize(){
        if (!user.getRole().getRole().equals("ADMIN")){
            tournaments.setVisible(false);
            teams.setVisible(false);
            players.setVisible(false);
            history.setVisible(false);
        }
    }
    public void showTournamentSearch(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("tournamentScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Tournaments");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showTournamentInput(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("tournamentInputScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Edit tournaments");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showPlayerBrowse(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("playerScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Players");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showPlayerInput(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("playerInputScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Edit players");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showTeamBrowse(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("teamScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Teams");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showTeamInput(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("teamInputScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Edit teams");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    public void showChanges(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("historyScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("History");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
    @FXML
    protected void onLogoutClick(){
        LoginController.setLoggedUser();
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("loginScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController.getStage().setTitle("Login");
        MainController.getStage().setScene(scene);
        MainController.getStage().show();
    }
}
