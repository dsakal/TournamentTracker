package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static com.java.project.tournamenttracker.database.DatabaseAccess.*;
import static com.java.project.tournamenttracker.files.FileAccess.*;

public class TournamentBrowseController {
    private User user = LoginController.getLoggedUser();
    private List<Tournament> tournaments;
    private List<Team> teams;
    {
        try {
            tournaments = getTournamentsFromDatabase();
            teams = getTeamsFromDatabase();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML private TextField search;
    @FXML private GridPane gridPane;
    @FXML private HBox singleBracket;
    @FXML private HBox doubleBracket;
    @FXML private Label nameLabel;
    @FXML private Label locationLabel;
    @FXML private Label durationLabel;
    @FXML private Label winnerLabel;
    @FXML private TableView<Tournament> tournamentsTableView;
    @FXML private TableColumn<Tournament, String> tournamentNameTableColumn;
    private Tournament selectedTournament;
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
    @FXML private Label singleBracketFinalTeam1;
    @FXML private Label singleBracketFinalTeam2;
    @FXML private Label singleBracketFinalTeam1Score;
    @FXML private Label singleBracketFinalTeam2Score;
    @FXML private Label singleBracketSemiFinal1Team1;
    @FXML private Label singleBracketSemiFinal1Team2;
    @FXML private Label singleBracketSemiFinal1Team1Score;
    @FXML private Label singleBracketSemiFinal1Team2Score;
    @FXML private Label singleBracketSemiFinal2Team1;
    @FXML private Label singleBracketSemiFinal2Team2;
    @FXML private Label singleBracketSemiFinal2Team1Score;
    @FXML private Label singleBracketSemiFinal2Team2Score;
    @FXML private Label singleBracketQuarterFinal1Team1;
    @FXML private Label singleBracketQuarterFinal1Team2;
    @FXML private Label singleBracketQuarterFinal1Team1Score;
    @FXML private Label singleBracketQuarterFinal1Team2Score;
    @FXML private Label singleBracketQuarterFinal2Team1;
    @FXML private Label singleBracketQuarterFinal2Team2;
    @FXML private Label singleBracketQuarterFinal2Team1Score;
    @FXML private Label singleBracketQuarterFinal2Team2Score;
    @FXML private Label singleBracketQuarterFinal3Team1Score;
    @FXML private Label singleBracketQuarterFinal3Team2Score;
    @FXML private Label singleBracketQuarterFinal3Team1;
    @FXML private Label singleBracketQuarterFinal3Team2;
    @FXML private Label singleBracketQuarterFinal4Team1Score;
    @FXML private Label singleBracketQuarterFinal4Team2Score;
    @FXML private Label singleBracketQuarterFinal4Team1;
    @FXML private Label singleBracketQuarterFinal4Team2;
    @FXML private Label singleBracketRound1Team1;
    @FXML private Label singleBracketRound1Team2;
    @FXML private Label singleBracketRound2Team1;
    @FXML private Label singleBracketRound2Team2;
    @FXML private Label singleBracketRound3Team1;
    @FXML private Label singleBracketRound3Team2;
    @FXML private Label singleBracketRound4Team1;
    @FXML private Label singleBracketRound4Team2;
    @FXML private Label singleBracketRound5Team1;
    @FXML private Label singleBracketRound5Team2;
    @FXML private Label singleBracketRound6Team1;
    @FXML private Label singleBracketRound6Team2;
    @FXML private Label singleBracketRound7Team1;
    @FXML private Label singleBracketRound7Team2;
    @FXML private Label singleBracketRound8Team1;
    @FXML private Label singleBracketRound8Team2;
    @FXML private Label singleBracketRound1Team1Score;
    @FXML private Label singleBracketRound1Team2Score;
    @FXML private Label singleBracketRound2Team1Score;
    @FXML private Label singleBracketRound2Team2Score;
    @FXML private Label singleBracketRound3Team1Score;
    @FXML private Label singleBracketRound3Team2Score;
    @FXML private Label singleBracketRound4Team1Score;
    @FXML private Label singleBracketRound4Team2Score;
    @FXML private Label singleBracketRound5Team1Score;
    @FXML private Label singleBracketRound5Team2Score;
    @FXML private Label singleBracketRound6Team1Score;
    @FXML private Label singleBracketRound6Team2Score;
    @FXML private Label singleBracketRound7Team1Score;
    @FXML private Label singleBracketRound7Team2Score;
    @FXML private Label singleBracketRound8Team1Score;
    @FXML private Label singleBracketRound8Team2Score;

    @FXML
    public void initialize() throws FileNotFoundException {
        singleBracket.setVisible(false);
        doubleBracket.setVisible(false);

        gridPane.setAlignment(Pos.CENTER);
        Image image = new Image(new FileInputStream("dat\\img\\background_big.jpg"));
        BackgroundImage backgroundimage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundimage);
        gridPane.setBackground(background);

        tournamentNameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tournamentsTableView.setItems(FXCollections.observableArrayList(tournaments));

        /*tournamentsTableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                selectedTournament = tournamentsTableView.getSelectionModel().getSelectedItem();
                showTournamentDetails(selectedTournament);
            }
        });*/
        tournamentsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTournament = tournamentsTableView.getSelectionModel().getSelectedItem();
                showTournamentDetails(selectedTournament);
            }
        });
    }

    private void showTournamentDetails(Tournament tournament) {
        setVisibility();
        singleBracket.setVisible(true);
        nameLabel.setText(tournament.getName());
        locationLabel.setText(tournament.getVenue() + ", " + tournament.getLocation());
        durationLabel.setText(tournament.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + " - " + tournament.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        if (tournament.getTeams().size() > 8){
            for (Result r : tournament.getResults()){
                if (r.bracketStage().equals("singleBracketFinal")){
                    if (Integer.parseInt(r.teamOneWins()) > Integer.parseInt(r.teamTwoWins())){
                        winnerLabel.setText(r.teamOne().getName());
                    }
                    else {
                        winnerLabel.setText(r.teamTwo().getName());
                    }
                }
            }

        }
        else if (tournament.getTeams().size() < 16){
            for (Result r : tournament.getResults()){
                if (r.bracketStage().equals("singleBracketSemiFinal1")){
                    if (Integer.parseInt(r.teamOneWins()) > Integer.parseInt(r.teamTwoWins())){
                        winnerLabel.setText(r.teamOne().getName());
                    }
                    else {
                        winnerLabel.setText(r.teamTwo().getName());
                    }
                }
            }
        }

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
                    singleBracketFinalTeam1.setText(r.teamOne().getName());
                    singleBracketFinalTeam2.setText(r.teamTwo().getName());
                    singleBracketFinalTeam1Score.setText(r.teamOneWins());
                    singleBracketFinalTeam2Score.setText(r.teamTwoWins());
                }
                case "singleBracketSemiFinal1" -> {
                    singleBracketSemiFinal1.setVisible(true);
                    singleBracketSemiFinal1Team1.setText(r.teamOne().getName());
                    singleBracketSemiFinal1Team2.setText(r.teamTwo().getName());
                    singleBracketSemiFinal1Team1Score.setText(r.teamOneWins());
                    singleBracketSemiFinal1Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketSemiFinal2" -> {
                    singleBracketSemiFinal2.setVisible(true);
                    singleBracketSemiFinal2Team1.setText(r.teamOne().getName());
                    singleBracketSemiFinal2Team2.setText(r.teamTwo().getName());
                    singleBracketSemiFinal2Team1Score.setText(r.teamOneWins());
                    singleBracketSemiFinal2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal1" -> {
                    singleBracketquarterFinal1.setVisible(true);
                    singleBracketQuarterFinal1line1.setVisible(true);
                    singleBracketQuarterFinal1line2.setVisible(true);
                    singleBracketQuarterFinal1line3.setVisible(true);
                    singleBracketQuarterFinal1Team1.setText(r.teamOne().getName());
                    singleBracketQuarterFinal1Team2.setText(r.teamTwo().getName());
                    singleBracketQuarterFinal1Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal1Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal2" -> {
                    singleBracketquarterFinal2.setVisible(true);
                    singleBracketQuarterFinal2line1.setVisible(true);
                    singleBracketQuarterFinal2line2.setVisible(true);
                    singleBracketQuarterFinal2line3.setVisible(true);
                    singleBracketQuarterFinal2Team1.setText(r.teamOne().getName());
                    singleBracketQuarterFinal2Team2.setText(r.teamTwo().getName());
                    singleBracketQuarterFinal2Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal3" -> {
                    singleBracketquarterFinal3.setVisible(true);
                    singleBracketQuarterFinal3line1.setVisible(true);
                    singleBracketQuarterFinal3Line2.setVisible(true);
                    singleBracketQuarterFinal3line3.setVisible(true);
                    singleBracketQuarterFinal3Team1.setText(r.teamOne().getName());
                    singleBracketQuarterFinal3Team2.setText(r.teamTwo().getName());
                    singleBracketQuarterFinal3Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal3Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketQuarterFinal4" -> {
                    singleBracketquarterFinal4.setVisible(true);
                    singleBracketQuarterFinal4line1.setVisible(true);
                    singleBracketQuarterFinal4line2.setVisible(true);
                    singleBracketQuarterFinal4line3.setVisible(true);
                    singleBracketQuarterFinal4Team1.setText(r.teamOne().getName());
                    singleBracketQuarterFinal4Team2.setText(r.teamTwo().getName());
                    singleBracketQuarterFinal4Team1Score.setText(r.teamOneWins());
                    singleBracketQuarterFinal4Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound1" -> {
                    singleBracketRound1.setVisible(true);
                    singleBracketRound1line1.setVisible(true);
                    singleBracketRound1line2.setVisible(true);
                    singleBracketRound1line3.setVisible(true);
                    singleBracketRound1Team1.setText(r.teamOne().getName());
                    singleBracketRound1Team2.setText(r.teamTwo().getName());
                    singleBracketRound1Team1Score.setText(r.teamOneWins());
                    singleBracketRound1Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound2" -> {
                    singleBracketRound2.setVisible(true);
                    singleBracketRound2line1.setVisible(true);
                    singleBracketRound2line2.setVisible(true);
                    singleBracketRound2line3.setVisible(true);
                    singleBracketRound2Team1.setText(r.teamOne().getName());
                    singleBracketRound2Team2.setText(r.teamTwo().getName());
                    singleBracketRound2Team1Score.setText(r.teamOneWins());
                    singleBracketRound2Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound3" -> {
                    singleBracketRound3.setVisible(true);
                    singleBracketRound3line1.setVisible(true);
                    singleBracketRound3line2.setVisible(true);
                    singleBracketRound3line3.setVisible(true);
                    singleBracketRound3Team1.setText(r.teamOne().getName());
                    singleBracketRound3Team2.setText(r.teamTwo().getName());
                    singleBracketRound3Team1Score.setText(r.teamOneWins());
                    singleBracketRound3Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound4" -> {
                    singleBracketRound4.setVisible(true);
                    singleBracketRound4line1.setVisible(true);
                    singleBracketRound4line2.setVisible(true);
                    singleBracketRound4line3.setVisible(true);
                    singleBracketRound4Team1.setText(r.teamOne().getName());
                    singleBracketRound4Team2.setText(r.teamTwo().getName());
                    singleBracketRound4Team1Score.setText(r.teamOneWins());
                    singleBracketRound4Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound5" -> {
                    singleBracketRound5.setVisible(true);
                    singleBracketRound5line1.setVisible(true);
                    singleBracketRound5line2.setVisible(true);
                    singleBracketRound5line3.setVisible(true);
                    singleBracketRound5Team1.setText(r.teamOne().getName());
                    singleBracketRound5Team2.setText(r.teamTwo().getName());
                    singleBracketRound5Team1Score.setText(r.teamOneWins());
                    singleBracketRound5Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound6" -> {
                    singleBracketRound6.setVisible(true);
                    singleBracketRound6line1.setVisible(true);
                    singleBracketRound6line2.setVisible(true);
                    singleBracketRound6line3.setVisible(true);
                    singleBracketRound6Team1.setText(r.teamOne().getName());
                    singleBracketRound6Team2.setText(r.teamTwo().getName());
                    singleBracketRound6Team1Score.setText(r.teamOneWins());
                    singleBracketRound6Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound7" -> {
                    singleBracketRound7.setVisible(true);
                    singleBracketRound7line1.setVisible(true);
                    singleBracketRound7line2.setVisible(true);
                    singleBracketRound7line3.setVisible(true);
                    singleBracketRound7Team1.setText(r.teamOne().getName());
                    singleBracketRound7Team2.setText(r.teamTwo().getName());
                    singleBracketRound7Team1Score.setText(r.teamOneWins());
                    singleBracketRound7Team2Score.setText(r.teamTwoWins());
                }
                case "singleBracketRound8" -> {
                    singleBracketRound8.setVisible(true);
                    singleBracketRound8line1.setVisible(true);
                    singleBracketRound8line2.setVisible(true);
                    singleBracketRound8line3.setVisible(true);
                    singleBracketRound8Team1.setText(r.teamOne().getName());
                    singleBracketRound8Team2.setText(r.teamTwo().getName());
                    singleBracketRound8Team1Score.setText(r.teamOneWins());
                    singleBracketRound8Team2Score.setText(r.teamTwoWins());
                }
            }
        }

    }
    private void setVisibility(){
        singleBracketFinal.setVisible(false);
        singleBracketFinalLine1.setVisible(false);
        singleBracketFinalLine2.setVisible(false);
        singleBracketFinalLine3.setVisible(false);
        singleBracketFinalLine4.setVisible(false);
        singleBracketFinalLine5.setVisible(false);
        singleBracketFinalLine6.setVisible(false);
        singleBracketSemiFinal1.setVisible(false);
        singleBracketSemiFinal2.setVisible(false);
        singleBracketquarterFinal1.setVisible(false);
        singleBracketquarterFinal2.setVisible(false);
        singleBracketquarterFinal3.setVisible(false);
        singleBracketquarterFinal4.setVisible(false);
        singleBracketRound1.setVisible(false);
        singleBracketRound2.setVisible(false);
        singleBracketRound3.setVisible(false);
        singleBracketRound4.setVisible(false);
        singleBracketRound5.setVisible(false);
        singleBracketRound6.setVisible(false);
        singleBracketRound7.setVisible(false);
        singleBracketRound8.setVisible(false);
        singleBracketRound1line1.setVisible(false);
        singleBracketRound1line2.setVisible(false);
        singleBracketRound1line3.setVisible(false);
        singleBracketRound2line1.setVisible(false);
        singleBracketRound2line2.setVisible(false);
        singleBracketRound2line3.setVisible(false);
        singleBracketRound3line1.setVisible(false);
        singleBracketRound3line2.setVisible(false);
        singleBracketRound3line3.setVisible(false);
        singleBracketRound4line1.setVisible(false);
        singleBracketRound4line2.setVisible(false);
        singleBracketRound4line3.setVisible(false);
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
        singleBracketQuarterFinal1line1.setVisible(false);
        singleBracketQuarterFinal1line2.setVisible(false);
        singleBracketQuarterFinal1line3.setVisible(false);
        singleBracketQuarterFinal2line1.setVisible(false);
        singleBracketQuarterFinal2line2.setVisible(false);
        singleBracketQuarterFinal2line3.setVisible(false);
        singleBracketQuarterFinal3line1.setVisible(false);
        singleBracketQuarterFinal3Line2.setVisible(false);
        singleBracketQuarterFinal3line3.setVisible(false);
        singleBracketQuarterFinal4line1.setVisible(false);
        singleBracketQuarterFinal4line2.setVisible(false);
        singleBracketQuarterFinal4line3.setVisible(false);
    }
    @FXML
    protected void onSearchClick(){
        singleBracket.setVisible(false);
        tournamentsTableView.getSelectionModel().clearSelection();
        nameLabel.setText("");
        locationLabel.setText("");
        durationLabel.setText("");
        winnerLabel.setText("");
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
}