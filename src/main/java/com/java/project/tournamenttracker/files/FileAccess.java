package com.java.project.tournamenttracker.files;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class FileAccess {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final String PLAYERS_FILE = "dat\\players.dat";
    private static final String TEAMS_FILE = "dat\\teams.dat";
    private static final String TOURNAMENT_FILE = "dat\\tournaments.dat";
    private static final String USERS_FILE = "dat\\users.txt";
    private static final String CHANGES_FILE = "dat\\changes.dat";

    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (Scanner in = new Scanner(new File(USERS_FILE))) {
            while (in.hasNextLine()){
                String[] split = in.nextLine().split(",");
                Optional<Role> role = Optional.empty();
                switch (split[2]) {
                    case "ADMIN" -> role = Optional.of(Role.ADMIN);
                    case "USER" -> role = Optional.of(Role.USER);
                    case "GUEST" -> role = Optional.of(Role.GUEST);
                }
                users.add(new User.UserBuilder()
                        .addUsername(split[0])
                        .addPassword(split[1])
                        .addRole(role.get())
                        .build());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return users;
    }
    public static void serializePlayers(List<Player> players){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PLAYERS_FILE))) {
            try {
                out.writeObject(players);
            } catch (NotSerializableException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static List<Player> deserializePlayers(){
        List<Player>  players = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PLAYERS_FILE))) {
            players = (List<Player>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
        return players;
    }
    public static void serializeTeams(List<Team> teams){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEAMS_FILE))) {
            try {
                out.writeObject(teams);
            } catch (NotSerializableException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static List<Team> deserializeTeams(){
        List<Team>  teams = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEAMS_FILE))) {
            teams = (List<Team>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
        return teams;
    }
    public static void serializeTournaments(List<Tournament> tournaments){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TOURNAMENT_FILE))) {
            try {
                out.writeObject(tournaments);
            } catch (NotSerializableException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static List<Tournament> deserializeTournaments(){
        List<Tournament>  tournaments = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TOURNAMENT_FILE))) {
            tournaments = (List<Tournament>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex);
        }
        return tournaments;
    }
    public static void writeUsers(List<User> users){
        for (User u : users){
            try (PrintWriter out = new PrintWriter(new FileOutputStream(new File("dat\\users.txt"), true))) {
                out.println(u.getUsername() + "," + u.hashPassword(u.getPassword()) + "," + u.getRole().getRole());
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
    public static void serializeChanges(List<Changes> changes){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE))) {
            try {
                out.writeObject(changes);
            } catch (NotSerializableException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            String msg = "Error serializing changes!";
            logger.error(msg, e);
        }
    }
    public static List<Changes> deserializeChanges(){
        List<Changes>  changes = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CHANGES_FILE))) {
            changes = (List<Changes>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            String msg = "Error deserializing changes!";
            logger.error(msg, ex);
        }
        return changes;
    }
}