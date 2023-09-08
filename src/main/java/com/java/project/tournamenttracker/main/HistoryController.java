package com.java.project.tournamenttracker.main;

import com.java.project.tournamenttracker.entities.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.java.project.tournamenttracker.files.FileAccess.deserializeChanges;

public class HistoryController {
    Image background;
    {
        try {
            background = new Image(new FileInputStream("dat\\img\\background_mid.jpg"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private GridPane gridPane;

    @FXML
    private TableView<Changes> changesTableView;
    @FXML
    private TableColumn<Changes, String> beforeTableColumn;
    @FXML
    private TableColumn<Changes, String> afterTableColumn;
    @FXML
    private TableColumn<Changes, String> userTableColumn;
    @FXML
    private TableColumn<Changes, String> dateTimeTableColumn;
    private List<Changes> changes = deserializeChanges();
    @FXML
    public void initialize(){
        loadDefaultBackground();

        beforeTableColumn.setCellFactory(param -> createWrappedTextCell());
        afterTableColumn.setCellFactory(param -> createWrappedTextCell());

        beforeTableColumn.setCellValueFactory(cellData -> {
            Changes changes = cellData.getValue();
            if (changes.getBefore() instanceof Team) {
                Team team = (Team) changes.getBefore();
                return new SimpleStringProperty(team.getName() + "\n" + team.getLocation() + "\n" + team.getDateFounded() + "\n"
                        + team.getPlayers().toString().replace("[", "").replace("]", "").replace(",",""));
            } else if (changes.getBefore() instanceof Player) {
                Player player = (Player) changes.getBefore();
                return new SimpleStringProperty(player.toString());
            } else if (changes.getBefore() instanceof Tournament) {
                Tournament tournament = (Tournament) changes.getBefore();
                return new SimpleStringProperty(tournament.toString());
            } else if (changes.getBefore() instanceof String) {
                String str = (String) changes.getBefore();
                return new SimpleStringProperty(str);
            }else {
                return new SimpleStringProperty("Unknown Type");
            }
        });
        afterTableColumn.setCellValueFactory(cellData -> {
            Changes changes = cellData.getValue();
            if (changes.getAfter() instanceof Team) {
                Team team = (Team) changes.getAfter();
                return new SimpleStringProperty(team.getName() + "\n" + team.getLocation() + "\n" + team.getDateFounded() + "\n"
                        + team.getPlayers().toString().replace("[", "").replace("]", "").replace(",",""));
            } else if (changes.getAfter() instanceof Player) {
                Player player = (Player) changes.getAfter();
                return new SimpleStringProperty(player.toString());
            } else if (changes.getAfter() instanceof Tournament) {
                Tournament tournament = (Tournament) changes.getAfter();
                return new SimpleStringProperty(tournament.toString());
            } else if (changes.getAfter() instanceof String) {
                String str = (String) changes.getAfter();
                return new SimpleStringProperty(str);
            }else {
                return new SimpleStringProperty("Unknown Type");
            }
        });
        //beforeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBefore().toString()));
        //afterTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAfter().toString()));
        userTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        dateTimeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss"))));


        changesTableView.setItems(FXCollections.observableArrayList(changes));
    }
    public void loadDefaultBackground() {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setBackground(new Background(new BackgroundImage(background,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
    }
    private TableCell<Changes, String> createWrappedTextCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Text text = new Text(item);
                    text.setStyle("-fx-text-alignment:justify;");
                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                    setGraphic(text);
                }
            }
        };
    }

}
