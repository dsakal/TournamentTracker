package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.Player;
import com.java.project.tournamenttracker.entities.Team;
import com.java.project.tournamenttracker.entities.User;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.java.project.tournamenttracker.files.FileAccess.*;
import static com.java.project.tournamenttracker.database.DatabaseAccess.*;

public class TeamBrowseController {
    private User user = LoginController.getLoggedUser();
    @FXML
    private TextField searchTeamName;
    @FXML
    private TextField searchRegion;
    @FXML
    private TextField searchPlayerName;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label regionLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TableView<Player> playersTableView;
    @FXML
    private TableColumn<Player, String> playerNameTableColumn;
    @FXML
    private TableColumn<Player, String> playerPositionTableColumn;
    @FXML
    private TableView<Team> teamsTableView;
    @FXML
    private TableColumn<Team, String> teamTableColumn;
    @FXML
    private ObservableList<Team> teamObservableList;
    @FXML
    private ImageView teamImageView;

    private List<Player> players;
    private List<Team> teams;

    {
        try {
            teams = getTeamsFromDatabase();
            players = getPlayersFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private Team selectedTeam;
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

        teamTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        teamObservableList = FXCollections.observableArrayList(teams);

        teamsTableView.setItems(teamObservableList);

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTeam = teamsTableView.getSelectionModel().getSelectedItem();
                showTeamDetails(selectedTeam);
            }
        });

        playersTableView.setSelectionModel(null);
    }
    @FXML
    protected void onSearchClick(){
        clearTeam();
        String name = searchTeamName.getText();
        String region = searchRegion.getText();
        String playerName = searchPlayerName.getText();
        List<Team> teamList = teams.stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(t -> t.getLocation().toLowerCase().contains(region.toLowerCase()))
                .filter(t -> t.getPlayers().stream().anyMatch(player -> player.getName().toLowerCase().contains(playerName.toLowerCase()) ||
                        player.getLastName().contains(playerName.toLowerCase()) ||
                        player.getNickname().toLowerCase().contains(playerName.toLowerCase()))
                )
                .toList();
        FilteredList<Team> filteredTeams = new FilteredList<>(FXCollections.observableArrayList(teamList));
        teamsTableView.setItems(filteredTeams);
    }
    public void showTeamDetails(Team team){
        regionLabel.setText(team.getLocation());
        dateLabel.setText(team.getDateFounded().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        try {
            teamImageView.setImage(new Image(new FileInputStream("dat\\img\\" +team.getName().toLowerCase() + ".jpg")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        playerNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName() + " '" + cellData.getValue().getNickname() + "' " + cellData.getValue().getLastName()));
        playerPositionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition().getDescription()));
        playersTableView.setItems(new FilteredList<>(FXCollections.observableArrayList(team.getPlayers())));
    }
    public void clearTeam(){
        teamsTableView.getSelectionModel().clearSelection();
        regionLabel.setText("");
        dateLabel.setText("");
        teamImageView.setImage(null);
        playersTableView.setItems(null);
    }
}
