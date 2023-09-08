package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.exceptions.FileDeletionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.java.project.tournamenttracker.database.DatabaseAccess.*;
import static com.java.project.tournamenttracker.files.FileAccess.*;

public class PlayerInputController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private User user = LoginController.getLoggedUser();
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField search;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField nicknameTextField;
    @FXML
    private ChoiceBox<String> positionChoiceBox;
    @FXML
    private TextField nationalityTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Player> playersTableView;
    @FXML
    private TableColumn<Player, String> playerNameTableColumn;
    @FXML
    private TableColumn<Player, String> playerDateTableColumn;
    @FXML
    private TableColumn<Player, String> positionTableColumn;
    @FXML
    private TableColumn<Player, String> nationTableColumn;
    @FXML
    private ObservableList<Player> playerObservableList;
    @FXML
    private ImageView imagePreview;
    private List<Player> players;
    private List<Changes> changes = deserializeChanges();
    {
        try {
            players = getPlayersFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Team> teams;

    {
        try {
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    private Player selectedPlayer;
    private final FileChooser fileChooser = new FileChooser();
    private Optional<File> selectedFile = Optional.empty();

    @FXML
    public void initialize() throws FileNotFoundException {
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
        List<String> positionString = Arrays.asList("Carry", "Middle", "Offlane", "Soft support", "Hard support");
        for (String s : positionString){
            positionChoiceBox.getItems().add(s);
        }
        positionChoiceBox.getSelectionModel().select(0);

        playerNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName() + " '" + cellData.getValue().getNickname() + "' " + cellData.getValue().getLastName()));
        playerDateTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))));
        positionTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosition().getDescription()));
        nationTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationality()));

        playerObservableList = FXCollections.observableArrayList(players);

        playersTableView.setItems(playerObservableList);

        /*playersTableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
                fillFields(selectedPlayer);
            }
        });*/
        playersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPlayer = playersTableView.getSelectionModel().getSelectedItem();
                fillFields(selectedPlayer);
            }
        });
    }

    @FXML
    protected void onSearchClick(){
        clearPlayer();
        imagePreview.setImage(null);
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
        playersTableView.refresh();
    }
    @FXML
    protected void newPlayerClick(){
        Player newPlayer = getDataFromScreen();
        if (newPlayer != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to add a new player?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    players.add(newPlayer);
                    try {
                        saveNewPlayerToDatabase(newPlayer);
                    } catch (DatabaseException e) {
                        String msg = "Error while adding new player to database!";
                        logger.error(msg, e);
                    }
                    serializePlayers(players);

                    if (selectedFile.isPresent()) {
                        try {
                            String destinationDirectory = "dat\\img\\";
                            String newFileName = newPlayer.getNickname() + ".jpg";
                            Path sourcePath = selectedFile.get().toPath();
                            Path destinationPath = new File(destinationDirectory, newFileName).toPath();
                            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    changes.add(new Changes<>("New Input", newPlayer, user, LocalDateTime.now()));
                    serializeChanges(changes);
                    refreshTable();
                    playersTableView.refresh();
                }
            });
        }
    }
    @FXML
    protected void editPlayerClick() {
        Player newPlayer = getDataFromScreen();
        if (newPlayer != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to edit the selected player?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    Integer id = selectedPlayer.getId();
                    newPlayer.setId(id);
                    Optional<Player> before = Optional.empty();
                    for (Player p : players){
                        if (p.getId().equals(id)){
                            before = Optional.of(p);
                        }
                    }
                    try {
                        editPlayerToDatabase(newPlayer);
                    } catch (DatabaseException e) {
                        String msg = "Error while editing player in database!";
                        logger.error(msg, e);
                    }
                    playersTableView.refresh();
                    changes.add(new Changes<>(before.get(), newPlayer, user, LocalDateTime.now()));
                    serializeChanges(changes);

                    for (Player p : players){
                        if (p.getId().equals(id)){
                            p.setName(newPlayer.getName());
                            p.setLastName(newPlayer.getLastName());
                            p.setNickname(newPlayer.getNickname());
                            p.setPosition(newPlayer.getPosition());
                            p.setNationality(newPlayer.getNationality());
                            p.setImage(newPlayer.getNickname() + ".jpg");
                            p.setDateOfBirth(newPlayer.getDateOfBirth());
                        }
                    }
                    serializePlayers(players);
                    if (selectedFile.isPresent()) {
                        try {
                            String destinationDirectory = "dat\\img\\";
                            String newFileName = newPlayer.getNickname() + ".jpg";
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
    protected void deletePlayerClick() {
        Player player = getDataFromScreen();
        if (player != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the selected player?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    Integer id = selectedPlayer.getId();
                    String name = nameTextField.getText();
                    String lastName = lastNameTextField.getText();
                    String nickname = nicknameTextField.getText();
                    String pos = positionChoiceBox.getValue();
                    Optional<Position> position = Optional.empty();
                    switch (pos.toLowerCase()) {
                        case "carry" -> position = Optional.of(Position.CARRY);
                        case "middle" -> position = Optional.of(Position.MID);
                        case "offlane" -> position = Optional.of(Position.OFFLANE);
                        case "soft support"-> position = Optional.of(Position.SOFT_SUPPORT);
                        case "hard support" -> position = Optional.of(Position.HARD_SUPPORT);
                    }
                    String nation = nationalityTextField.getText();
                    LocalDate date = datePicker.getValue();
                    Optional<Player> before = Optional.empty();
                    for (Player p : players){
                        if (p.getId().equals(id)){
                            before = Optional.of(p);
                            p.setName(name);
                            p.setLastName(lastName);
                            p.setNickname(nickname);
                            p.setPosition(position.get());
                            p.setNationality(nation);
                            p.setImage(nickname + ".jpg");
                            p.setDateOfBirth(date);
                        }
                    }
                    serializePlayers(players);
                    try {
                        deletePlayerFromDatabase(new Player(id, name, nickname, lastName, date, nation, position.get(), nickname + ".jpg"));
                    } catch (DatabaseException e) {
                        String msg = "Error while deleteing player from database!";
                        logger.error(msg, e);
                    }
                    playersTableView.refresh();
                    refreshTable();
                    changes.add(new Changes<>(before.get(), "Deleted", user, LocalDateTime.now()));
                    serializeChanges(changes);
                    imagePreview.setImage(null);
                    try{
                        deleteImage("dat\\img\\" + nickname + ".jpg");
                    }catch (FileDeletionException e){
                        String msg = "Could not delete image!";
                        logger.error(msg, e);
                        throw new FileDeletionException(msg, e);
                    }
                }
            });
            clearPlayer();
        }
    }
    @FXML
    protected void selectImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        selectedFile = Optional.ofNullable(fileChooser.showOpenDialog(new Stage()));
        selectedFile.ifPresent(file -> imagePreview.setImage(new Image(file.toString())));
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
    public void fillFields(Player player){
        nameTextField.setText(player.getName());
        lastNameTextField.setText(player.getLastName());
        nicknameTextField.setText(player.getNickname());
        positionChoiceBox.getSelectionModel().select(player.getPosition().getNumericalValue()-1);
        nationalityTextField.setText(player.getNationality());
        datePicker.setValue(player.getDateOfBirth());
        selectedFile = Optional.empty();
        imagePreview.setImage(new Image("file:dat\\img\\" + player.getImage()));
    }
    public void refreshTable() {
        try {
            players = getPlayersFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        playersTableView.setItems(FXCollections.observableArrayList(players));
    }
    public void deleteImage(String path) throws FileDeletionException{
        File imageToDelete = new File(path);
        boolean deleted = imageToDelete.delete();
        if (!deleted){
            throw new FileDeletionException();
        }
    }
    public void clearPlayer(){
        playersTableView.getSelectionModel().clearSelection();
        nameTextField.setText("");
        lastNameTextField.setText("");
        nicknameTextField.setText("");
        nationalityTextField.setText("");
        positionChoiceBox.getSelectionModel().select(0);
        datePicker.getEditor().clear();
        selectedFile = Optional.empty();
        imagePreview.setImage(null);
    }
    public Player getDataFromScreen(){
        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();
        if (name.isEmpty()){
            errorMessages.append("Name must be entered!\n");
        }
        String lastName = lastNameTextField.getText();
        if (lastName.isEmpty()){
            errorMessages.append("Last name must be entered!\n");
        }
        String nickname = nicknameTextField.getText();
        if (nickname.isEmpty()){
            errorMessages.append("Nickname name must be entered!\n");
        }
        String pos = positionChoiceBox.getValue();
        if (pos.isEmpty()){
            errorMessages.append("A position name must be selected!\n");
        }
        String nation = nationalityTextField.getText();
        if (nation.isEmpty()){
            errorMessages.append("Nationality name must be entered!\n");
        }
        LocalDate date = datePicker.getValue();
        if (date == null){
            errorMessages.append("Date of birth name must be selected!\n");
        }
        Optional<Position> position = Optional.empty();
        switch (pos.toLowerCase()) {
            case "carry" -> position = Optional.of(Position.CARRY);
            case "middle" -> position = Optional.of(Position.MID);
            case "offlane" -> position = Optional.of(Position.OFFLANE);
            case "soft support"-> position = Optional.of(Position.SOFT_SUPPORT);
            case "hard support" -> position = Optional.of(Position.HARD_SUPPORT);
        }
        if (!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error!");
            alert.setHeaderText("Not all data entered!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }else {
            return new Player(players.size()+1, name, nickname, lastName, date, nation, position.get(), nickname + ".jpg");
        }
        return null;
    }
}
