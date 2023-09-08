package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.Changes;
import com.java.project.tournamenttracker.entities.Player;
import com.java.project.tournamenttracker.entities.Team;
import com.java.project.tournamenttracker.entities.User;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.exceptions.FileDeletionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.java.project.tournamenttracker.database.DatabaseAccess.*;
import static com.java.project.tournamenttracker.files.FileAccess.*;

public class TeamInputController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private User user = LoginController.getLoggedUser();
    @FXML
    private TextField nameTextField;
    @FXML
    private ChoiceBox<String> regionChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField search;
    @FXML
    private GridPane gridPane;
    @FXML
    private TableView<Player> playersTableView;
    @FXML
    private TableColumn<Player, String> playerNameTableColumn;
    @FXML
    private TableView<Team> teamsTableView;
    @FXML
    private TableColumn<Team, String> teamNameTableColumn;
    @FXML
    private TableColumn<Team, String> teamRegionTableColumn;
    @FXML
    private TableColumn<Team, String> teamDateTableColumn;
    @FXML
    private TableColumn<Team, String> teamPlayersTableColumn;
    @FXML
    private ImageView imagePreview;
    private List<Player> players;
    private List<Team> teams;
    private List<Changes> changes = deserializeChanges();
    {
        try {
            players = getPlayersFromDatabase();
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    private Team selectedTeam;
    private final FileChooser fileChooser = new FileChooser();
    private Optional<File> selectedFile = Optional.empty();

    @FXML
    public void initialize() throws FileNotFoundException {
        List<String> regionString = Arrays.asList("Europe", "North America", "South America", "CIS", "China", "Southeast Asia");
        for (String s : regionString){
            regionChoiceBox.getItems().add(s);
        }
        regionChoiceBox.getSelectionModel().select(0);

        gridPane.setAlignment(Pos.CENTER);
        Image image = new Image(new FileInputStream("dat\\img\\background_mid.jpg"));
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        gridPane.setBackground(background);

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        teamNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        teamRegionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        teamDateTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFounded().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))));
        //teamPlayersTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlayers().toString().substring(1,cellData.getValue().getPlayers().toString().length() - 1)));
        teamPlayersTableColumn.setCellValueFactory(cellData -> {
            List<Player> players = cellData.getValue().getPlayers();
            if (players.isEmpty()) {
                return new SimpleStringProperty(""); // Return an empty string for no players
            } else {
                StringBuilder playerInfo = new StringBuilder();
                for (Player player : players) {
                    playerInfo.append(player.getName())
                            .append(" '")
                            .append(player.getNickname())
                            .append("' ")
                            .append(player.getLastName())
                            .append("\n");
                }
                return new SimpleStringProperty(playerInfo.toString().trim()); // Remove trailing newline
            }
        });

        teamsTableView.setItems(FXCollections.observableArrayList(teams));

        /*teamsTableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                selectedTeam = teamsTableView.getSelectionModel().getSelectedItem();
                showTeamDetails(selectedTeam);
            }
        });*/

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTeam = teamsTableView.getSelectionModel().getSelectedItem();
                showTeamDetails(selectedTeam);
            }
        });
        playerNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName() + " '" + cellData.getValue().getNickname() + "' " + cellData.getValue().getLastName()));
        playersTableView.setItems(FXCollections.observableArrayList(players));
        playersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playersTableView.getSelectionModel().select(0);
    }
    @FXML
    protected void onSearchClick(){
        clearTeam();
        String text = search.getText();
        List<Team> teamsList = teams.stream()
                .filter(t -> t.getName().toLowerCase().contains(text.toLowerCase()) ||
                        t.getLocation().toLowerCase().contains(text.toLowerCase()) ||
                        t.getDateFounded().toString().contains(text.toLowerCase()) ||
                        t.getPlayers().stream().anyMatch(player -> player.getName().toLowerCase().contains(text.toLowerCase()) ||
                                player.getLastName().contains(text.toLowerCase()) ||
                                player.getNickname().toLowerCase().contains(text.toLowerCase())))
                .toList();
        teamsTableView.setItems(new FilteredList<>(FXCollections.observableArrayList(teamsList)));
        teamsTableView.refresh();
    }
    @FXML
    protected void newTeamClick() {
        Team newTeam = getDataFromScreen();
        if(newTeam != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to add a new team?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {

                    teams.add(newTeam);
                    serializeTeams(teams);
                    try {
                        saveNewTeamToDatabase(newTeam);
                    } catch (DatabaseException e) {
                        String msg = "Error while adding new team to database!";
                        logger.error(msg, e);
                    }
                    teamsTableView.setItems(new FilteredList<>(FXCollections.observableArrayList(teams)));
                    if (selectedFile.isPresent()) {
                        try {
                            String destinationDirectory = "dat\\img\\";
                            String newFileName = newTeam.getName().toLowerCase() + ".jpg";
                            Path sourcePath = selectedFile.get().toPath();
                            Path destinationPath = new File(destinationDirectory, newFileName).toPath();
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    changes.add(new Changes<>("New Input", newTeam, user, LocalDateTime.now()));
                    serializeChanges(changes);
                    refreshTable();
                }
            });
        }
    }

    private Team getDataFromScreen() {
        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();
        if (name.isEmpty()){
            errorMessages.append("Name must be entered!\n");
        }
        List<Player> teamPlayers = playersTableView.getSelectionModel().getSelectedItems().stream().toList();
        if (teamPlayers.isEmpty()){
            errorMessages.append("Players must be selected!\n");
        }
        String region = regionChoiceBox.getValue();
        if (region.isEmpty()){
            errorMessages.append("Region must be selected!\n");
        }
        LocalDate date = datePicker.getValue();
        if (date == null){
            errorMessages.append("Date must be selected!\n");
        }
        if (!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error!");
            alert.setHeaderText("Not all data entered!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }else {
            return new Team(teams.size()+1, name, teamPlayers, region, date);
        }
        return null;
    }

    @FXML
    protected void editTeamClick() {
        Team newTeam = getDataFromScreen();
        if (newTeam != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to edit the selected team?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    String name = nameTextField.getText();
                    List<Player> teamPlayers = playersTableView.getSelectionModel().getSelectedItems().stream().toList();
                    String region = regionChoiceBox.getValue();
                    LocalDate date = datePicker.getValue();
                    Integer id = teamsTableView.getSelectionModel().getSelectedItem().getId();
                    Optional<Team> oldTeam = Optional.empty();
                    for (Team team : teams){
                        if (team.getId().equals(id)) {
                            oldTeam = Optional.of(team);
                        }
                    }
                    try {
                        editTeamToDatabase(new Team(id, name, teamPlayers, region, date));
                    } catch (DatabaseException e) {
                        String msg = "Error while editing team in database!";
                        logger.error(msg, e);
                    }
                    teamsTableView.refresh();
                    changes.add(new Changes<>(oldTeam.get(), new Team(id, name, teamPlayers, region, date), user, LocalDateTime.now()));
                    serializeChanges(changes);

                    for (Team team : teams){
                        if (team.getId().equals(id)) {
                            team.setName(name);
                            team.setLocation(region);
                            team.setDateFounded(date);
                            team.setPlayers(teamPlayers);
                        }
                    }
                    serializeTeams(teams);
                    if (selectedFile.isPresent()) {
                        try {
                            String destinationDirectory = "dat\\img\\";
                            String newFileName = name.toLowerCase() + ".jpg";
                            Path sourcePath = selectedFile.get().toPath();
                            Path destinationPath = new File(destinationDirectory, newFileName).toPath();
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    refreshTable();
                }
            });
        }
    }
    @FXML
    protected void deleteTeamClick() {
        Team newTeam = getDataFromScreen();
        if (newTeam != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the selected team?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    String name = nameTextField.getText();
                    List<Player> teamPlayers = playersTableView.getSelectionModel().getSelectedItems().stream().toList();
                    String region = regionChoiceBox.getValue();
                    LocalDate date = datePicker.getValue();
                    Integer id = teamsTableView.getSelectionModel().getSelectedItem().getId();
                    for (Team team : teams){
                        if (team.getId().equals(id)) {
                            team.setName(name);
                            team.setLocation(region);
                            team.setDateFounded(date);
                            team.setPlayers(teamPlayers);
                        }
                    }
                    try {
                        deleteTeamFromDatabase(new Team(id, name, teamPlayers, region, date));
                    } catch (DatabaseException e) {
                        String msg = "Error while deleting team from database!";
                        logger.error(msg, e);
                    }
                    serializeTeams(teams);
                    teamsTableView.refresh();
                    try{
                        deleteImage("dat\\img\\" + name.toLowerCase() + ".jpg");
                    }catch (FileDeletionException e){
                        String msg = "Could not delete image!";
                        logger.error(msg, e);
                    }
                    imagePreview.setImage(null);
                    changes.add(new Changes<>(new Team(teams.size()+1, name, teamPlayers, region, date), "Deleted", user, LocalDateTime.now()));
                    serializeChanges(changes);
                    refreshTable();
                }
            });
            clearTeam();
        }
    }
    public void showTeamDetails(Team team){
        playersTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        playersTableView.getSelectionModel().clearSelection();
        datePicker.setValue(team.getDateFounded());
        nameTextField.setText(team.getName());
        for (String s : regionChoiceBox.getItems()){
            if (s.equals(team.getLocation())){
                regionChoiceBox.getSelectionModel().select(s);
            }
        }
        for (Player p : playersTableView.getItems()){
            for (Player p2 : team.getPlayers()){
                if (p2.getId().equals(p.getId())){
                    playersTableView.getSelectionModel().select(p);
                }
            }
        }
        selectedFile = Optional.empty();
        imagePreview.setImage(new Image("file:dat\\img\\" + team.getName().toLowerCase() + ".jpg"));
    }
    public void refreshTable() {
        try {
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        teamsTableView.setItems(FXCollections.observableArrayList(teams));
    }
    @FXML
    protected void selectImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedFile = Optional.ofNullable(fileChooser.showOpenDialog(new Stage()));
        selectedFile.ifPresent(file -> imagePreview.setImage(new Image(file.toString())));
    }

    public void deleteImage(String path) throws FileDeletionException {
        File imageToDelete = new File(path);
        boolean deleted = imageToDelete.delete();
        if (!deleted){
            throw new FileDeletionException();
        }
    }
    public void clearTeam(){
        playersTableView.getSelectionModel().clearSelection();
        teamsTableView.getSelectionModel().clearSelection();
        datePicker.getEditor().clear();
        nameTextField.setText("");
        selectedFile = Optional.empty();
        imagePreview.setImage(null);
        regionChoiceBox.getSelectionModel().select(0);
    }
}
