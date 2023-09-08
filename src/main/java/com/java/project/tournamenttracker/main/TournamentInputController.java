package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.exceptions.LoginException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.java.project.tournamenttracker.database.DatabaseAccess.*;
import static com.java.project.tournamenttracker.files.FileAccess.*;

public class TournamentInputController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private User user = LoginController.getLoggedUser();
    private List<Tournament> tournaments;
    private final List<Team> teams;
    {
        try {
            tournaments = getTournamentsFromDatabase();
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    private Integer selectedNum;
    @FXML
    private ChoiceBox<Integer> numberTeamsChoiceBox;
    @FXML private GridPane gridPane;
    @FXML private TextField search;
    @FXML private HBox singleBracket;
    @FXML private TextArea nameTextField;
    @FXML private TextArea locationTextField;
    @FXML private TextArea venueTextField;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TableView<Tournament> tournamentsTableView;
    @FXML private TableColumn<Tournament, String> tournamentNameTableColumn;

    private Tournament selectedTournament;
    @FXML private TableView<Team> teamsTableView;
    @FXML private TableColumn<Team, String> teamNameTableColumn;
    @FXML private VBox singleBracketFinal;
    @FXML private VBox singleBracketSemiFinal1;
    @FXML private VBox singleBracketSemiFinal2;
    @FXML private VBox singleBracketquarterFinal1;
    @FXML private VBox singleBracketquarterFinal2;
    @FXML private VBox singleBracketquarterFinal3;
    @FXML private VBox singleBracketquarterFinal4;
    @FXML private VBox singleBracketRound1;
    @FXML private VBox singleBracketRound2;
    @FXML private VBox singleBracketRound3;
    @FXML private VBox singleBracketRound4;
    @FXML private VBox singleBracketRound5;
    @FXML private VBox singleBracketRound6;
    @FXML private VBox singleBracketRound7;
    @FXML private VBox singleBracketRound8;
    @FXML private Line singleBracketFinalLine1;
    @FXML private Line singleBracketFinalLine2;
    @FXML private Line singleBracketFinalLine3;
    @FXML private Line singleBracketFinalLine4;
    @FXML private Line singleBracketFinalLine5;
    @FXML private Line singleBracketFinalLine6;
    @FXML private Line singleBracketRound1line1;
    @FXML private Line singleBracketRound1line2;
    @FXML private Line singleBracketRound1line3;
    @FXML private Line singleBracketRound2line1;
    @FXML private Line singleBracketRound2line2;
    @FXML private Line singleBracketRound2line3;
    @FXML private Line singleBracketRound3line1;
    @FXML private Line singleBracketRound3line2;
    @FXML private Line singleBracketRound3line3;
    @FXML private Line singleBracketRound4line1;
    @FXML private Line singleBracketRound4line2;
    @FXML private Line singleBracketRound4line3;
    @FXML private Line singleBracketRound5line1;
    @FXML private Line singleBracketRound5line2;
    @FXML private Line singleBracketRound5line3;
    @FXML private Line singleBracketRound6line1;
    @FXML private Line singleBracketRound6line2;
    @FXML private Line singleBracketRound6line3;
    @FXML private Line singleBracketRound7line1;
    @FXML private Line singleBracketRound7line2;
    @FXML private Line singleBracketRound7line3;
    @FXML private Line singleBracketRound8line1;
    @FXML private Line singleBracketRound8line2;
    @FXML private Line singleBracketRound8line3;
    @FXML private Line singleBracketQuarterFinal1line1;
    @FXML private Line singleBracketQuarterFinal1line2;
    @FXML private Line singleBracketQuarterFinal1line3;
    @FXML private Line singleBracketQuarterFinal2line1;
    @FXML private Line singleBracketQuarterFinal2line2;
    @FXML private Line singleBracketQuarterFinal2line3;
    @FXML private Line singleBracketQuarterFinal3line1;
    @FXML private Line singleBracketQuarterFinal3Line2;
    @FXML private Line singleBracketQuarterFinal3line3;
    @FXML private Line singleBracketQuarterFinal4line1;
    @FXML private Line singleBracketQuarterFinal4line2;
    @FXML private Line singleBracketQuarterFinal4line3;
    @FXML private ChoiceBox<Team> singleBracketFinalTeam1;
    @FXML private ChoiceBox<Team> singleBracketFinalTeam2;
    @FXML private TextArea singleBracketFinalTeam1Score;
    @FXML private TextArea singleBracketFinalTeam2Score;
    @FXML private ChoiceBox<Team> singleBracketSemiFinal1Team1;
    @FXML private ChoiceBox<Team> singleBracketSemiFinal1Team2;
    @FXML private TextArea singleBracketSemiFinal1Team1Score;
    @FXML private TextArea singleBracketSemiFinal1Team2Score;
    @FXML private ChoiceBox<Team> singleBracketSemiFinal2Team1;
    @FXML private ChoiceBox<Team> singleBracketSemiFinal2Team2;
    @FXML private TextArea singleBracketSemiFinal2Team1Score;
    @FXML private TextArea singleBracketSemiFinal2Team2Score;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal1Team1;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal1Team2;
    @FXML private TextArea singleBracketQuarterFinal1Team1Score;
    @FXML private TextArea singleBracketQuarterFinal1Team2Score;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal2Team1;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal2Team2;
    @FXML private TextArea singleBracketQuarterFinal2Team1Score;
    @FXML private TextArea singleBracketQuarterFinal2Team2Score;
    @FXML private TextArea singleBracketQuarterFinal3Team1Score;
    @FXML private TextArea singleBracketQuarterFinal3Team2Score;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal3Team1;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal3Team2;
    @FXML private TextArea singleBracketQuarterFinal4Team1Score;
    @FXML private TextArea singleBracketQuarterFinal4Team2Score;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal4Team1;
    @FXML private ChoiceBox<Team> singleBracketQuarterFinal4Team2;
    @FXML private ChoiceBox<Team> singleBracketRound1Team1;
    @FXML private ChoiceBox<Team> singleBracketRound1Team2;
    @FXML private ChoiceBox<Team> singleBracketRound2Team1;
    @FXML private ChoiceBox<Team> singleBracketRound2Team2;
    @FXML private ChoiceBox<Team> singleBracketRound3Team1;
    @FXML private ChoiceBox<Team> singleBracketRound3Team2;
    @FXML private ChoiceBox<Team> singleBracketRound4Team1;
    @FXML private ChoiceBox<Team> singleBracketRound4Team2;
    @FXML private ChoiceBox<Team> singleBracketRound5Team1;
    @FXML private ChoiceBox<Team> singleBracketRound5Team2;
    @FXML private ChoiceBox<Team> singleBracketRound6Team1;
    @FXML private ChoiceBox<Team> singleBracketRound6Team2;
    @FXML private ChoiceBox<Team> singleBracketRound7Team1;
    @FXML private ChoiceBox<Team> singleBracketRound7Team2;
    @FXML private ChoiceBox<Team> singleBracketRound8Team1;
    @FXML private ChoiceBox<Team> singleBracketRound8Team2;
    @FXML private TextArea singleBracketRound1Team1Score;
    @FXML private TextArea singleBracketRound1Team2Score;
    @FXML private TextArea singleBracketRound2Team1Score;
    @FXML private TextArea singleBracketRound2Team2Score;
    @FXML private TextArea singleBracketRound3Team1Score;
    @FXML private TextArea singleBracketRound3Team2Score;
    @FXML private TextArea singleBracketRound4Team1Score;
    @FXML private TextArea singleBracketRound4Team2Score;
    @FXML private TextArea singleBracketRound5Team1Score;
    @FXML private TextArea singleBracketRound5Team2Score;
    @FXML private TextArea singleBracketRound6Team1Score;
    @FXML private TextArea singleBracketRound6Team2Score;
    @FXML private TextArea singleBracketRound7Team1Score;
    @FXML private TextArea singleBracketRound7Team2Score;
    @FXML private TextArea singleBracketRound8Team1Score;
    @FXML private TextArea singleBracketRound8Team2Score;
    private List<ChoiceBox<Team>> roundTeams = new ArrayList<>();

    private List<ChoiceBox<Team>> quarterTeams = new ArrayList<>();
    private List<ChoiceBox<Team>> semiTeams = new ArrayList<>();
    private List<TextArea> allScores = new ArrayList<>();
    private static List<Team> selectedTeams = new ArrayList<>();
    private List<Team> selectedChoiceBoxTeams = new ArrayList<>();
    private List<Team> removedTeams = new ArrayList<>();
    private List<Changes> changes = deserializeChanges();
    @FXML
    public void initialize() throws FileNotFoundException {
        roundTeams = new ArrayList<>(Arrays.asList(singleBracketRound1Team1, singleBracketRound1Team2, singleBracketRound2Team1, singleBracketRound2Team2,
            singleBracketRound3Team1, singleBracketRound3Team2, singleBracketRound4Team1, singleBracketRound4Team2, singleBracketRound5Team1, singleBracketRound5Team2, singleBracketRound6Team1,
            singleBracketRound6Team2,singleBracketRound7Team1, singleBracketRound7Team2, singleBracketRound8Team1, singleBracketRound8Team2));
        quarterTeams = new ArrayList<>(Arrays.asList(singleBracketQuarterFinal1Team1, singleBracketQuarterFinal1Team2, singleBracketQuarterFinal2Team1, singleBracketQuarterFinal2Team2,
                singleBracketQuarterFinal3Team1, singleBracketQuarterFinal3Team2, singleBracketQuarterFinal4Team1, singleBracketQuarterFinal4Team2));
        semiTeams = new ArrayList<>(Arrays.asList(singleBracketSemiFinal1Team1, singleBracketSemiFinal1Team2, singleBracketSemiFinal2Team1, singleBracketSemiFinal2Team2));
        allScores = new ArrayList<>(Arrays.asList(singleBracketRound1Team1Score, singleBracketRound1Team2Score, singleBracketRound2Team1Score, singleBracketRound2Team2Score,
                singleBracketRound3Team1Score, singleBracketRound3Team2Score, singleBracketRound4Team1Score, singleBracketRound4Team2Score, singleBracketRound5Team1Score, singleBracketRound5Team2Score, singleBracketRound6Team1Score,
                singleBracketRound6Team2Score,singleBracketRound7Team1Score, singleBracketRound7Team2Score, singleBracketRound8Team1Score, singleBracketRound8Team2Score, singleBracketQuarterFinal1Team1Score, singleBracketQuarterFinal1Team2Score,
                singleBracketQuarterFinal2Team1Score, singleBracketQuarterFinal2Team2Score, singleBracketQuarterFinal3Team1Score, singleBracketQuarterFinal3Team2Score, singleBracketQuarterFinal4Team1Score, singleBracketQuarterFinal4Team2Score,
                singleBracketSemiFinal1Team1Score, singleBracketSemiFinal1Team2Score, singleBracketSemiFinal2Team1Score, singleBracketSemiFinal2Team2Score, singleBracketFinalTeam1Score, singleBracketFinalTeam2Score));
        gridPane.setAlignment(Pos.CENTER);
        Image image = new Image(new FileInputStream("dat\\img\\background_big.jpg"));
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        gridPane.setBackground(background);
        for (int i = 8; i <= 16; i+=8){
            numberTeamsChoiceBox.getItems().add(i);
        }
        numberTeamsChoiceBox.getSelectionModel().select(1);
        numberTeamsChoiceBox.setOnAction(event -> {
            selectedNum = numberTeamsChoiceBox.getValue();
            setBracketSize(selectedNum);
        });
        teamNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        teamsTableView.setItems(FXCollections.observableArrayList(teams));
        teamsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        teamsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            clearChoiceBoxes();
            if (newSelection != null) {
                selectedTeams = teamsTableView.getSelectionModel().getSelectedItems();
                selectedChoiceBoxTeams = new ArrayList<>(selectedTeams);
                setTeamChoiceBoxes(selectedTeams);
            }
        });
        tournamentNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tournamentsTableView.setItems(FXCollections.observableArrayList(tournaments));
        /*tournamentsTableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                selectedTournament = tournamentsTableView.getSelectionModel().getSelectedItem();
                if (selectedTournament != null){
                    showTournamentDetails(selectedTournament);
                }
            }
        });*/
        tournamentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTournament = tournamentsTableView.getSelectionModel().getSelectedItem();
                showTournamentDetails(selectedTournament);
            }
        });
        numberTeamsChoiceBox.setOnAction(event -> {
            selectedNum = numberTeamsChoiceBox.getValue();
            setBracketSize(selectedNum);
        });

        singleBracketRound1Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound1Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                    listener(singleBracketRound1Team1, singleBracketRound1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal1Team1);
                }
            });
        });

        singleBracketRound1Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound1Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                    listener(singleBracketRound1Team1, singleBracketRound1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal1Team1);
                }
            });
        });
        singleBracketRound2Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound2Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                    listener(singleBracketRound2Team1, singleBracketRound2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal1Team2);
                }
            });
        });
        singleBracketRound2Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound2Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound2Team1, singleBracketRound2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal1Team2);
                }
            });
        });
        singleBracketRound3Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound3Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound3Team1, singleBracketRound3Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal2Team1);
                }
            });
        });
        singleBracketRound3Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound3Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound3Team1, singleBracketRound3Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal2Team1);
                }
            });
        });
        singleBracketRound4Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound4Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound4Team1, singleBracketRound4Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal2Team2);
                }
            });
        });
        singleBracketRound4Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound4Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound4Team1, singleBracketRound4Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal2Team2);
                }
            });
        });
        singleBracketRound5Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound5Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound5Team1, singleBracketRound5Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal3Team1);
                }
            });
        });
        singleBracketRound5Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound5Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound5Team1, singleBracketRound5Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal3Team1);
                }
            });
        });
        singleBracketRound6Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound6Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound6Team1, singleBracketRound6Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal3Team2);
                }
            });
        });
        singleBracketRound6Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound6Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound6Team1, singleBracketRound6Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal3Team2);
                }
            });
        });
        singleBracketRound7Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound7Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound7Team1, singleBracketRound7Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal4Team1);
                }
            });
        });
        singleBracketRound7Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound7Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound7Team1, singleBracketRound7Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal4Team1);
                }
            });
        });
        singleBracketRound8Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketRound8Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound8Team1, singleBracketRound8Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal4Team2);
                }
            });
        });
        singleBracketRound8Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketRound8Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketRound8Team1, singleBracketRound8Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketQuarterFinal4Team2);
                }
            });
        });
        singleBracketQuarterFinal1Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketQuarterFinal1Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal1Team1, singleBracketQuarterFinal1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal1Team1);
                }
            });
        });
        singleBracketQuarterFinal1Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketQuarterFinal1Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal1Team1, singleBracketQuarterFinal1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal1Team1);
                }
            });
        });
        singleBracketQuarterFinal2Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketQuarterFinal2Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal2Team1, singleBracketQuarterFinal2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal1Team2);
                }
            });
        });
        singleBracketQuarterFinal2Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketQuarterFinal2Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal2Team1, singleBracketQuarterFinal2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal1Team2);
                }
            });
        });
        singleBracketQuarterFinal3Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketQuarterFinal3Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal3Team1, singleBracketQuarterFinal3Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal2Team1);
                }
            });
        });
        singleBracketQuarterFinal3Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketQuarterFinal3Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal3Team1, singleBracketQuarterFinal3Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal2Team1);
                }
            });
        });
        singleBracketQuarterFinal4Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketQuarterFinal4Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal4Team1, singleBracketQuarterFinal4Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal2Team2);
                }
            });
        });
        singleBracketQuarterFinal4Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketQuarterFinal4Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketQuarterFinal4Team1, singleBracketQuarterFinal4Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketSemiFinal2Team2);
                }
            });
        });
        singleBracketSemiFinal1Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketSemiFinal1Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketSemiFinal1Team1, singleBracketSemiFinal1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketFinalTeam1);
                }
            });
        });
        singleBracketSemiFinal1Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketSemiFinal1Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketSemiFinal1Team1, singleBracketSemiFinal1Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketFinalTeam1);
                }
            });
        });
        singleBracketSemiFinal2Team1Score.textProperty().addListener((obs,old,text)->{
            singleBracketSemiFinal2Team2Score.textProperty().addListener((obs2,old2,text2)->{
                if (!text.equals("") && !text2.equals("")) {
                listener(singleBracketSemiFinal2Team1, singleBracketSemiFinal2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketFinalTeam2);
                }
            });
        });
        singleBracketSemiFinal2Team2Score.textProperty().addListener((obs2,old2,text2)->{
            singleBracketSemiFinal2Team1Score.textProperty().addListener((obs,old,text)->{
                if (!text.equals("") && !text2.equals("")) {
                    listener(singleBracketSemiFinal2Team1, singleBracketSemiFinal2Team2, Integer.parseInt(text), Integer.parseInt(text2), singleBracketFinalTeam2);
                }
            });
        });

        singleBracketRound1Team1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound1Team1.setValue(removedTeams.get(removedTeams.size() - 1));
        });
        singleBracketRound1Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound1Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound2Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound2Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound2Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound2Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound3Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound3Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound3Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound3Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound4Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound4Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound4Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound4Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound5Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound5Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound5Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound5Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound6Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound6Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound6Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound6Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound7Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound7Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound7Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound7Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound8Team1.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound8Team1.setValue(removedTeams.get(removedTeams.size()-1));
        });
        singleBracketRound8Team2.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            updateTeamChoiceBoxes(oldSelection, newSelection);
            singleBracketRound8Team2.setValue(removedTeams.get(removedTeams.size()-1));
        });
    }
    @FXML
    protected void onSearchClick(){
        teamsTableView.getSelectionModel().clearSelection();
        tournamentsTableView.getSelectionModel().clearSelection();
        singleBracket.setVisible(false);
        String text = search.getText();
        List<Tournament> tournamentsList = tournaments.stream()
                .filter(t -> t.getName().toLowerCase().contains(text.toLowerCase()) ||
                        t.getLocation().toLowerCase().contains(text.toLowerCase()) ||
                        t.getVenue().toLowerCase().contains(text.toLowerCase()) ||
                        t.getTeams().stream().anyMatch(team -> team.getName().toLowerCase().contains(text.toLowerCase())))
                .toList();

        FilteredList<Tournament> filteredTournaments = new FilteredList<>(FXCollections.observableArrayList(tournamentsList));
        tournamentsTableView.setItems(filteredTournaments);
        tournamentsTableView.refresh();
    }
    public void setBracketSize(Integer size){
        switch (size){
            case 8 ->{
                singleBracketFinal.setVisible(false);
                singleBracketFinalLine1.setVisible(false);
                singleBracketFinalLine2.setVisible(false);
                singleBracketFinalLine3.setVisible(false);
                singleBracketFinalLine4.setVisible(false);
                singleBracketFinalLine5.setVisible(false);
                singleBracketFinalLine6.setVisible(false);
                singleBracketRound5.setVisible(false);
                singleBracketRound6.setVisible(false);
                singleBracketRound7.setVisible(false);
                singleBracketRound8.setVisible(false);
                singleBracketRound5line1.setVisible(false);
                singleBracketRound5line2.setVisible(false);
                singleBracketRound5line3.setVisible(false);
                singleBracketRound6line1.setVisible(false);
                singleBracketRound6line2.setVisible(false);
                singleBracketRound6line3.setVisible(false);
                singleBracketRound7line1.setVisible(false);
                singleBracketRound7line2.setVisible(false);
                singleBracketRound7line3.setVisible(false);
                singleBracketRound8line1.setVisible(false);
                singleBracketRound8line2.setVisible(false);
                singleBracketRound8line3.setVisible(false);
                singleBracketquarterFinal3.setVisible(false);
                singleBracketquarterFinal4.setVisible(false);
                singleBracketQuarterFinal3line1.setVisible(false);
                singleBracketQuarterFinal3Line2.setVisible(false);
                singleBracketQuarterFinal3line3.setVisible(false);
                singleBracketQuarterFinal4line1.setVisible(false);
                singleBracketQuarterFinal4line2.setVisible(false);
                singleBracketQuarterFinal4line3.setVisible(false);
                singleBracketSemiFinal2.setVisible(false);
            }
            case 16 ->{
                singleBracketFinal.setVisible(true);
                singleBracketquarterFinal3.setVisible(true);
                singleBracketquarterFinal4.setVisible(true);
                singleBracketQuarterFinal3line1.setVisible(true);
                singleBracketQuarterFinal3Line2.setVisible(true);
                singleBracketQuarterFinal3line3.setVisible(true);
                singleBracketQuarterFinal4line1.setVisible(true);
                singleBracketQuarterFinal4line2.setVisible(true);
                singleBracketQuarterFinal4line3.setVisible(true);
                singleBracketFinalLine1.setVisible(true);
                singleBracketFinalLine2.setVisible(true);
                singleBracketFinalLine3.setVisible(true);
                singleBracketFinalLine4.setVisible(true);
                singleBracketFinalLine5.setVisible(true);
                singleBracketFinalLine6.setVisible(true);
                singleBracketSemiFinal2.setVisible(true);
                singleBracketRound5.setVisible(true);
                singleBracketRound6.setVisible(true);
                singleBracketRound7.setVisible(true);
                singleBracketRound8.setVisible(true);
                singleBracketRound5line1.setVisible(true);
                singleBracketRound5line2.setVisible(true);
                singleBracketRound5line3.setVisible(true);
                singleBracketRound6line1.setVisible(true);
                singleBracketRound6line2.setVisible(true);
                singleBracketRound6line3.setVisible(true);
                singleBracketRound7line1.setVisible(true);
                singleBracketRound7line2.setVisible(true);
                singleBracketRound7line3.setVisible(true);
                singleBracketRound8line1.setVisible(true);
                singleBracketRound8line2.setVisible(true);
                singleBracketRound8line3.setVisible(true);
            }
        }
    }
    @FXML
    protected void onEditClick() throws DatabaseException {
        Tournament data = getDataFromScreen();
        if (data != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to edit the selected tournament?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    data.setId(selectedTournament.getId());
                    Optional<Tournament> oldTournament = Optional.empty();
                    for (Tournament t : tournaments){
                        if (data.getId().equals(t.getId())){
                            oldTournament = Optional.of(t);
                        }
                    }
                    try {
                        editTournamentToDatabase(data);
                    } catch (DatabaseException e) {
                        String msg = "Error while editing tournament in database!";
                        logger.error(msg, e);
                    }

                    tournamentsTableView.refresh();
                    changes.add(new Changes<>(oldTournament.get(), data, user, LocalDateTime.now()));
                    serializeChanges(changes);

                    for (Tournament t : tournaments){
                        if (data.getId().equals(t.getId())){
                            t.setName(data.getName());
                            t.setLocation(data.getLocation());
                            t.setResults(data.getResults());
                            t.setTeams(data.getTeams());
                            t.setVenue(data.getVenue());
                            t.setStartDate(data.getStartDate());
                            t.setEndDate(data.getEndDate());
                        }
                    }
                    serializeTournaments(tournaments);
                }
            });
            refreshTable();
        }
    }
    @FXML
    protected void onNewClick() throws DatabaseException {
        Tournament data = getDataFromScreen();
        if (data != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to add a new tournament?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    data.setId(tournaments.size()+1);
                    tournaments.add(data);
                    tournamentsTableView.refresh();
                    serializeTournaments(tournaments);
                    try {
                        saveNewTournamentToDatabase(data);
                    } catch (DatabaseException e) {
                        String msg = "Error while adding new tournament to database!";
                        logger.error(msg, e);
                    }
                    tournamentsTableView.refresh();
                    changes.add(new Changes<>("New Input", data, user, LocalDateTime.now()));
                    serializeChanges(changes);
                }
            });
            refreshTable();
        }
    }
    @FXML
    protected void onDeleteClick() throws DatabaseException {
        Tournament data = getDataFromScreen();
        if (data != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete the selected tournament?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == yesButton) {
                    data.setId(selectedTournament.getId());
                    tournaments.remove(data);
                    serializeTournaments(tournaments);
                    try {
                        deleteTournamentFromDatabase(data);
                    } catch (DatabaseException e) {
                        String msg = "Error while deleting tournament from database!";
                        logger.error(msg, e);
                    }
                    changes.add(new Changes<>(data, "Deleted", user, LocalDateTime.now()));
                    serializeChanges(changes);
                }
            });
            refreshTable();
        }
    }
    public void showTournamentDetails(Tournament tournament){
        singleBracket.setVisible(true);
        teamsTableView.getSelectionModel().clearSelection();
        nameTextField.setText(tournament.getName());
        locationTextField.setText(tournament.getLocation());
        venueTextField.setText(tournament.getVenue());
        startDate.setValue(tournament.getStartDate());
        endDate.setValue(tournament.getEndDate());
        if (tournament.getTeams().size() == 8){
            numberTeamsChoiceBox.getSelectionModel().select(0);
            setBracketSize(8);
        }
        else {
            numberTeamsChoiceBox.getSelectionModel().select(1);
            setBracketSize(16);
        }
        for (Team t : teamsTableView.getItems()){
            for (Team t2 : tournament.getTeams()){
                if (t2.getId().equals(t.getId())){
                    teamsTableView.getSelectionModel().select(t);
                }
            }
        }
        setTeamChoiceBoxes(tournament.getTeams());
        for (Result r : tournament.getResults()){
            switch (r.bracketStage()){
                case "singleBracketFinal" -> {
                    singleBracketFinal.setVisible(true);
                    singleBracketFinalLine1.setVisible(true);
                    singleBracketFinalLine2.setVisible(true);
                    singleBracketFinalLine3.setVisible(true);
                    singleBracketFinalLine4.setVisible(true);
                    singleBracketFinalLine5.setVisible(true);
                    singleBracketFinalLine6.setVisible(true);
                    singleBracketFinalTeam1.getSelectionModel().select(r.teamOne());
                    singleBracketFinalTeam2.getSelectionModel().select(r.teamTwo());
                    singleBracketFinalTeam1Score.setText(r.teamOneWins());
                    singleBracketFinalTeam2Score.setText(r.teamTwoWins());
                }
                case "singleBracketSemiFinal1" -> {
                    singleBracketSemiFinal1.setVisible(true);
                    singleBracketSemiFinal1Team1.getSelectionModel().select(r.teamOne());
                    singleBracketSemiFinal1Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketSemiFinal1Team1Score.setText(r.teamOneWins());
                    singleBracketSemiFinal1Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketSemiFinal2" -> {
                    singleBracketSemiFinal2.setVisible(true);
                    singleBracketSemiFinal2Team1.getSelectionModel().select(r.teamOne());
                    singleBracketSemiFinal2Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketSemiFinal2Team1Score.setText(r.teamOneWins());
                    singleBracketSemiFinal2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal1" -> {
                    singleBracketquarterFinal1.setVisible(true);
                    singleBracketQuarterFinal1line1.setVisible(true);
                    singleBracketQuarterFinal1line2.setVisible(true);
                    singleBracketQuarterFinal1line3.setVisible(true);
                    singleBracketQuarterFinal1Team1.getSelectionModel().select(r.teamOne());
                    singleBracketQuarterFinal1Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketQuarterFinal1Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal1Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal2" -> {
                    singleBracketquarterFinal2.setVisible(true);
                    singleBracketQuarterFinal2line1.setVisible(true);
                    singleBracketQuarterFinal2line2.setVisible(true);
                    singleBracketQuarterFinal2line3.setVisible(true);
                    singleBracketQuarterFinal2Team1.getSelectionModel().select(r.teamOne());
                    singleBracketQuarterFinal2Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketQuarterFinal2Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal3" -> {
                    singleBracketquarterFinal3.setVisible(true);
                    singleBracketQuarterFinal3line1.setVisible(true);
                    singleBracketQuarterFinal3Line2.setVisible(true);
                    singleBracketQuarterFinal3line3.setVisible(true);
                    singleBracketQuarterFinal3Team1.getSelectionModel().select(r.teamOne());
                    singleBracketQuarterFinal3Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketQuarterFinal3Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal3Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal4" -> {
                    singleBracketquarterFinal4.setVisible(true);
                    singleBracketQuarterFinal4line1.setVisible(true);
                    singleBracketQuarterFinal4line2.setVisible(true);
                    singleBracketQuarterFinal4line3.setVisible(true);
                    singleBracketQuarterFinal4Team1.getSelectionModel().select(r.teamOne());
                    singleBracketQuarterFinal4Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketQuarterFinal4Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal4Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound8" -> {
                    singleBracketRound8.setVisible(true);
                    singleBracketRound8line1.setVisible(true);
                    singleBracketRound8line2.setVisible(true);
                    singleBracketRound8line3.setVisible(true);
                    singleBracketRound8Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound8Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound8Team1Score.setText(r.teamOneWins());
                    singleBracketRound8Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound7" -> {
                    singleBracketRound7.setVisible(true);
                    singleBracketRound7line1.setVisible(true);
                    singleBracketRound7line2.setVisible(true);
                    singleBracketRound7line3.setVisible(true);
                    singleBracketRound7Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound7Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound7Team1Score.setText(r.teamOneWins());
                    singleBracketRound7Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound6" -> {
                    singleBracketRound6.setVisible(true);
                    singleBracketRound6line1.setVisible(true);
                    singleBracketRound6line2.setVisible(true);
                    singleBracketRound6line3.setVisible(true);
                    singleBracketRound6Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound6Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound6Team1Score.setText(r.teamOneWins());
                    singleBracketRound6Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound5" -> {
                    singleBracketRound5.setVisible(true);
                    singleBracketRound5line1.setVisible(true);
                    singleBracketRound5line2.setVisible(true);
                    singleBracketRound5line3.setVisible(true);
                    singleBracketRound5Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound5Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound5Team1Score.setText(r.teamOneWins());
                    singleBracketRound5Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound4" -> {
                    singleBracketRound4.setVisible(true);
                    singleBracketRound4line1.setVisible(true);
                    singleBracketRound4line2.setVisible(true);
                    singleBracketRound4line3.setVisible(true);
                    singleBracketRound4Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound4Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound4Team1Score.setText(r.teamOneWins());
                    singleBracketRound4Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound3" -> {
                    singleBracketRound3.setVisible(true);
                    singleBracketRound3line1.setVisible(true);
                    singleBracketRound3line2.setVisible(true);
                    singleBracketRound3line3.setVisible(true);
                    singleBracketRound3Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound3Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound3Team1Score.setText(r.teamOneWins());
                    singleBracketRound3Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound2" -> {
                    singleBracketRound2.setVisible(true);
                    singleBracketRound2line1.setVisible(true);
                    singleBracketRound2line2.setVisible(true);
                    singleBracketRound2line3.setVisible(true);
                    singleBracketRound2Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound2Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound2Team1Score.setText(r.teamOneWins());
                    singleBracketRound2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound1" -> {
                    singleBracketRound1.setVisible(true);
                    singleBracketRound1line1.setVisible(true);
                    singleBracketRound1line2.setVisible(true);
                    singleBracketRound1line3.setVisible(true);
                    singleBracketRound1Team1.getSelectionModel().select(r.teamOne());
                    singleBracketRound1Team2.getSelectionModel().select(r.teamTwo());
                    singleBracketRound1Team1Score.setText(r.teamOneWins());
                    singleBracketRound1Team2Score.setText(r.teamTwoWins());
                }
            }
        }
    }
    public Tournament getDataFromScreen() throws DatabaseException {
        StringBuilder errorMessages = new StringBuilder();

        String name = nameTextField.getText();
        if (name.isEmpty()){
            errorMessages.append("Name must be entered!\n");
        }
        String location = locationTextField.getText();
        if (location.isEmpty()){
            errorMessages.append("Location must be entered!\n");
        }
        String venue = venueTextField.getText();
        if (venue.isEmpty()){
            errorMessages.append("Venue must be entered!\n");
        }
        LocalDate start = startDate.getValue();
        if (start == null) {
            errorMessages.append("A start date must be selected!\n");
        }
        LocalDate end = endDate.getValue();
        if (end == null) {
            errorMessages.append("An end date must be selected!\n");
        }
        List<Team> selectedTeams = teamsTableView.getSelectionModel().getSelectedItems().stream().toList();
        if (selectedTeams.isEmpty()){
            errorMessages.append("Teams must be selected!\n");
        }
        List<Result> results = new ArrayList<>();
        for (int i = 1; i <= numberTeamsChoiceBox.getValue()-1; i++){
            switch (i){
                case 1 -> results.add(new Result(singleBracketRound1Team1.getValue(), singleBracketRound1Team2.getValue(), singleBracketRound1Team1Score.getText(), singleBracketRound1Team2Score.getText(), "singleBracketRound1", 0));
                case 2 -> results.add(new Result(singleBracketRound2Team1.getValue(), singleBracketRound2Team2.getValue(), singleBracketRound2Team1Score.getText(), singleBracketRound2Team2Score.getText(), "singleBracketRound2", 0));
                case 3 -> results.add(new Result(singleBracketRound3Team1.getValue(), singleBracketRound3Team2.getValue(), singleBracketRound3Team1Score.getText(), singleBracketRound3Team2Score.getText(), "singleBracketRound3", 0));
                case 4 -> results.add(new Result(singleBracketRound4Team1.getValue(), singleBracketRound4Team2.getValue(), singleBracketRound4Team1Score.getText(), singleBracketRound4Team2Score.getText(), "singleBracketRound4", 0));
                case 5 -> results.add(new Result(singleBracketQuarterFinal1Team1.getValue(), singleBracketQuarterFinal1Team2.getValue(), singleBracketQuarterFinal1Team1Score.getText(), singleBracketQuarterFinal1Team2Score.getText(), "singleBracketQuarterFinal1", 0));
                case 6 -> results.add(new Result(singleBracketQuarterFinal2Team1.getValue(), singleBracketQuarterFinal2Team2.getValue(), singleBracketQuarterFinal2Team1Score.getText(), singleBracketQuarterFinal2Team2Score.getText(), "singleBracketQuarterFinal2", 0));
                case 7 -> results.add(new Result(singleBracketSemiFinal1Team1.getValue(), singleBracketSemiFinal1Team2.getValue(), singleBracketSemiFinal1Team1Score.getText(), singleBracketSemiFinal1Team2Score.getText(), "singleBracketSemiFinal1", 0));
                case 8 -> results.add(new Result(singleBracketRound5Team1.getValue(), singleBracketRound5Team2.getValue(), singleBracketRound5Team1Score.getText(), singleBracketRound5Team2Score.getText(), "singleBracketRound5", 0));
                case 9 -> results.add(new Result(singleBracketRound6Team1.getValue(), singleBracketRound6Team2.getValue(), singleBracketRound6Team1Score.getText(), singleBracketRound6Team2Score.getText(), "singleBracketRound6", 0));
                case 10 -> results.add(new Result(singleBracketRound7Team1.getValue(), singleBracketRound7Team2.getValue(), singleBracketRound7Team1Score.getText(), singleBracketRound7Team2Score.getText(), "singleBracketRound7", 0));
                case 11 -> results.add(new Result(singleBracketRound8Team1.getValue(), singleBracketRound8Team2.getValue(), singleBracketRound8Team1Score.getText(), singleBracketRound8Team2Score.getText(), "singleBracketRound8", 0));
                case 12 -> results.add(new Result(singleBracketQuarterFinal3Team1.getValue(), singleBracketQuarterFinal3Team2.getValue(), singleBracketQuarterFinal3Team1Score.getText(), singleBracketQuarterFinal3Team2Score.getText(), "singleBracketQuarterFinal3", 0));
                case 13 -> results.add(new Result(singleBracketQuarterFinal4Team1.getValue(), singleBracketQuarterFinal4Team2.getValue(), singleBracketQuarterFinal4Team1Score.getText(), singleBracketQuarterFinal4Team2Score.getText(), "singleBracketQuarterFinal4", 0));
                case 14 -> results.add(new Result(singleBracketSemiFinal2Team1.getValue(), singleBracketSemiFinal2Team2.getValue(), singleBracketSemiFinal2Team1Score.getText(), singleBracketSemiFinal2Team2Score.getText(), "singleBracketSemiFinal2", 0));
                case 15 -> results.add(new Result(singleBracketFinalTeam1.getValue(), singleBracketFinalTeam2.getValue(), singleBracketFinalTeam1Score.getText(), singleBracketFinalTeam2Score.getText(), "singleBracketFinal", 0));
            }
        }
        int check = 0;
        for (int i = 0; i < numberTeamsChoiceBox.getValue()-1; i++){
            if (results.get(i).teamOne() == null || results.get(i).teamTwo() == null || results.get(i).teamOneWins() == null || results.get(i).teamTwoWins() == null){
                check = 1;
            }
        }
        if (check == 1){
            errorMessages.append("All bracket data must be entered!\n");
        }
        if (!errorMessages.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input error!");
            alert.setHeaderText("Not all data entered!");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }else {
            return new Tournament(0, name, location, venue, start, end, selectedTeams, results);
        }
        return null;
    }

    public void setTeamChoiceBoxes(List<Team> selectedTeams){
        for (ChoiceBox<Team> t : roundTeams){
            t.setItems(FXCollections.observableArrayList(selectedTeams));
        }
        for (ChoiceBox<Team> t : quarterTeams){
            t.setItems(FXCollections.observableArrayList(selectedTeams));
        }
        for (ChoiceBox<Team> t : semiTeams){
            t.setItems(FXCollections.observableArrayList(selectedTeams));
        }
        singleBracketFinalTeam1.setItems(FXCollections.observableArrayList(selectedTeams));
        singleBracketFinalTeam2.setItems(FXCollections.observableArrayList(selectedTeams));
    }
    public void clearChoiceBoxes(){
        for (TextArea t : allScores){
            t.clear();
        }
        for (ChoiceBox<Team> t : quarterTeams){
            t.valueProperty().set(null);
        }
        for (ChoiceBox<Team> t : semiTeams){
            t.valueProperty().set(null);
        }
        for (ChoiceBox<Team> t : roundTeams){
            t.setValue(null);
        }
        singleBracketFinalTeam1.valueProperty().set(null);
        singleBracketFinalTeam2.valueProperty().set(null);
    }
    public void updateTeamChoiceBoxes(Team oldTeam, Team newTeam){
        List<Team> oldAdd = new ArrayList<>();
        List<Team> oldRemove = new ArrayList<>();
        List<Team> newAdd = new ArrayList<>();
        List<Team> newRemove = new ArrayList<>();
        if (newTeam != null){
            if (oldTeam != null) {
                if (removedTeams.contains(oldTeam) && selectedTeams.contains(oldTeam)) {
                    oldAdd.add(oldTeam);
                    oldRemove.add(oldTeam);
                }
            }
            for (Team t : selectedChoiceBoxTeams){
                if (t.getName().equals(newTeam.getName())){
                    newAdd.add(newTeam);
                    newRemove.add(newTeam);
                }
            }
            removedTeams.addAll(newAdd);
            removedTeams.removeAll(oldRemove);
            selectedChoiceBoxTeams.addAll(oldAdd);
            selectedChoiceBoxTeams.removeAll(newRemove);
            setTeamChoiceBoxes(selectedChoiceBoxTeams);
        }else {
            setTeamChoiceBoxes(new ArrayList<>());
        }
    }
    public void listener(ChoiceBox<Team> team1, ChoiceBox<Team> team2, Integer team1score, Integer team2score, ChoiceBox<Team> teamSelect){
        if (team1score != null && team2score != null){
            if (team1score > team2score){
                teamSelect.getSelectionModel().select(team1.getValue());
            }
            else {
                teamSelect.getSelectionModel().select(team2.getValue());
            }
        }
    }
    @FXML
    protected void onClearSelectionClick(){
        clearChoiceBoxes();
        setBracketSize(numberTeamsChoiceBox.getValue());
        teamsTableView.getSelectionModel().clearSelection();
        tournamentsTableView.getSelectionModel().clearSelection();
        nameTextField.clear();
        locationTextField.clear();
        venueTextField.clear();
        startDate.getEditor().clear();
        endDate.getEditor().clear();

        selectedTeams = new ArrayList<>();
        setTeamChoiceBoxes(selectedTeams);
    }
    public void refreshTable() {
        try {
            tournaments = getTournamentsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        tournamentsTableView.setItems(FXCollections.observableArrayList(tournaments));
    }
}
