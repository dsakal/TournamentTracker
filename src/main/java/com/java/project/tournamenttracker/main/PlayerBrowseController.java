package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.Player;
import com.java.project.tournamenttracker.entities.Team;
import com.java.project.tournamenttracker.entities.User;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.threads.YoungestPlayerThread;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.java.project.tournamenttracker.files.FileAccess.*;
import static com.java.project.tournamenttracker.database.DatabaseAccess.*;

public class PlayerBrowseController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private User user = LoginController.getLoggedUser();
    Image background;
    {
        try {
            background = new Image(new FileInputStream("dat\\img\\background.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private TextField search;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label bornLabel;
    @FXML
    private Label nationalityLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private Label teamLabel;
    @FXML
    private TableView<Player> playersTableView;
    @FXML
    private TableColumn<Player, String> playerTableColumn;
    @FXML
    private ImageView playerImageView;

    private List<Player> players;
    private List<Team> teams;
    {
        try {
            players = getPlayersFromDatabase();
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    private Player selectedPlayer;


    @FXML
    public void initialize(){
        loadDefaultBackground();
        playerTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName() + " '" + cellData.getValue().getNickname() + "' " + cellData.getValue().getLastName()));
        playersTableView.setItems(FXCollections.observableArrayList(players));

        playersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
                try {
                    showPlayer(selectedPlayer);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    @FXML
    protected void onSearchClick(){
        clearPlayer();
        loadDefaultBackground();
        String text = search.getText();
        List<Player> playersList = players.stream()
                .filter(p -> p.getName().toLowerCase().contains(text.toLowerCase()) ||
                        p.getLastName().toLowerCase().contains(text.toLowerCase()) ||
                        p.getNickname().toLowerCase().contains(text.toLowerCase()) ||
                        p.getPosition().getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        p.getNationality().toLowerCase().contains(text.toLowerCase()) ||
                        findTeam(p).getName().toLowerCase().contains(text.toLowerCase())
                )
                .toList();
        FilteredList<Player> filteredPlayers = new FilteredList<>(FXCollections.observableArrayList(playersList));
        playersTableView.setItems(filteredPlayers);
    }
    public Team findTeam(Player player){
        Optional<Team> team = Optional.empty();
        for (Team t : teams){
            for (Player p : t.getPlayers()){
                if (p.getId().equals(player.getId())){
                    team = Optional.of(t);
                }
            }
        }
        if (team.isEmpty()){
            return new Team(0, "", new ArrayList<>(),"", LocalDate.parse("11.11.2000.", DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        }
        else return team.get();
    }
    public void showPlayer(Player player) throws FileNotFoundException {
        gridPane.setAlignment(Pos.CENTER);
        if (!findTeam(player).getName().equals("")){
            Image image = new Image(new FileInputStream("dat\\img\\"+ findTeam(player).getName().toLowerCase() + ".jpg"));
            gridPane.setBackground(new Background(new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT)));
        }
        else {
            loadDefaultBackground();
        }
        nameLabel.setText(player.getName());
        lastNameLabel.setText(player.getLastName());
        nicknameLabel.setText(player.getNickname());
        bornLabel.setText(player.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        nationalityLabel.setText(player.getNationality());
        positionLabel.setText(player.getPosition().getDescription());
        if (findTeam(player) != null) {
            teamLabel.setText(findTeam(player).getName());
        }
        else {
            teamLabel.setText("");
        }
        playerImageView.setImage(new Image(new FileInputStream("dat\\img\\" + player.getImage())));
    }
    public void clearPlayer(){
        playersTableView.getSelectionModel().clearSelection();
        nameLabel.setText("");
        lastNameLabel.setText("");
        nicknameLabel.setText("");
        bornLabel.setText("");
        nationalityLabel.setText("");
        positionLabel.setText("");
        teamLabel.setText("");
        playerImageView.setImage(null);
    }
    public void loadDefaultBackground() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setBackground(new Background(new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }
}
