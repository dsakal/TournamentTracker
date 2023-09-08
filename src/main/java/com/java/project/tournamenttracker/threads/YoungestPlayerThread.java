package com.java.project.tournamenttracker.threads;

import com.java.project.tournamenttracker.database.DatabaseAccess;
import com.java.project.tournamenttracker.entities.Player;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class YoungestPlayerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Override
    public synchronized void run() {
        Player youngest = null;
        try {
            List<Player> players = DatabaseAccess.getPlayersFromDatabase();
            for (Player p : players) {
                if (youngest != null){
                    if (p.getDateOfBirth().isAfter(youngest.getDateOfBirth())) {
                        youngest = p;
                    }
                }
                else {
                    youngest = p;
                }
            }
            MainController.getStage().setTitle("Youngest player: " + youngest.getName()
                    + " '" + youngest.getNickname() + "' " + youngest.getLastName()
                    + " (" + youngest.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + ")");
        } catch (DatabaseException ex) {
            String msg = "Error while accessing players database!";
            logger.error(msg, ex);
        }
    }
}
