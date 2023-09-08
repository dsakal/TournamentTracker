package com.java.project.tournamenttracker.database;

import com.java.project.tournamenttracker.entities.*;
import com.java.project.tournamenttracker.exceptions.DatabaseException;
import com.java.project.tournamenttracker.main.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseAccess {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private static final String DATABASE_FILE = "dat\\database.properties";
    public static Connection connectToDatabase() throws SQLException, IOException {
        Properties svojstva = new Properties();
        svojstva.load(new FileReader(DATABASE_FILE));
        String urlBazePodataka = svojstva.getProperty("URL");
        String korisnickoIme = svojstva.getProperty("username");
        String lozinka = svojstva.getProperty("password");
        Connection veza = DriverManager.getConnection(urlBazePodataka,
                korisnickoIme,lozinka);
        return veza;
    }
    public static List<Player> getPlayersFromDatabase() throws DatabaseException {
        List<Player> players = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PLAYER");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String nickname = resultSet.getString("nickname");
                String lastName = resultSet.getString("last_name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                String nationality = resultSet.getString("nationality");
                String pos = resultSet.getString("position");
                Optional<Position> position = Optional.empty();
                switch (pos) {
                    case "Carry" -> position = Optional.of(Position.CARRY);
                    case "Middle" -> position = Optional.of(Position.MID);
                    case "Offlane" -> position = Optional.of(Position.OFFLANE);
                    case "Soft support" -> position = Optional.of(Position.SOFT_SUPPORT);
                    case "Hard support" -> position = Optional.of(Position.HARD_SUPPORT);
                }
                players.add(new Player(id, name, nickname, lastName, dateOfBirth, nationality, position.get(), nickname.toLowerCase() + ".jpg"));
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while accessing players database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
        return players;
    }
    public static void saveNewPlayerToDatabase(Player player) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PLAYER(name, nickname, last_name, date_of_birth, nationality, position) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.setString(3, player.getLastName());
            preparedStatement.setString(4, player.getDateOfBirth().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, player.getNationality());
            preparedStatement.setString(6, player.getPosition().getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String msg = "Error while adding player to database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void editPlayerToDatabase(Player player) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE PLAYER SET name = ?, nickname = ?, last_name = ?, date_of_birth = ?, nationality = ?, position = ? WHERE ID=" + player.getId());
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getNickname());
            preparedStatement.setString(3, player.getLastName());
            preparedStatement.setString(4, player.getDateOfBirth().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, player.getNationality());
            preparedStatement.setString(6, player.getPosition().getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String msg = "Error while editing player database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static List<Team> getTeamsFromDatabase() throws DatabaseException {
        List<Team> teams = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TEAM");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                LocalDate dateFounded = resultSet.getDate("date_founded").toLocalDate();
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM TEAM_PLAYER WHERE team_id = " + id);
                List<Player> teamPlayers = new ArrayList<>();
                while (resultSet2.next()) {
                    Integer playerID = resultSet2.getInt("player_id");
                    List<Player> players = getPlayersFromDatabase().stream()
                            .filter(p -> p.getId().equals(playerID))
                            .toList();
                    teamPlayers.add(players.get(0));
                }
                teams.add(new Team(id, name, teamPlayers, location, dateFounded));
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while accessing teams database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
        return teams;
    }
    public static void saveNewTeamToDatabase(Team team) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TEAM(name, location, date_founded) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, team.getName());
            preparedStatement.setString(2, team.getLocation());
            preparedStatement.setString(3, team.getDateFounded().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int teamId;
            if (generatedKeys.next()) {
                teamId = generatedKeys.getInt(1);
            } else {
                throw new DatabaseException("Failed to retrieve the generated team ID.");
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO TEAM_PLAYER(team_id, player_id) VALUES (?, ?)");
            for (Player p : team.getPlayers()){
                preparedStatement2.setInt(1, teamId);
                preparedStatement2.setInt(2, p.getId());
                preparedStatement2.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while adding team to database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void editTeamToDatabase(Team team) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM TEAM_PLAYER WHERE TEAM_ID = " + team.getId());
            preparedStatement3.executeUpdate();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TEAM SET name = ?, location =?, date_founded =? WHERE ID=" + team.getId());
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO TEAM_PLAYER(team_id, player_id) VALUES (?, ?)");
            preparedStatement.setString(1, team.getName());
            preparedStatement.setString(2, team.getLocation());
            preparedStatement.setString(3, team.getDateFounded().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.executeUpdate();
            for (Player p : team.getPlayers()){
                preparedStatement2.setInt(1, team.getId());
                preparedStatement2.setInt(2, p.getId());
                preparedStatement2.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while updating team database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static List<Tournament>  getTournamentsFromDatabase() throws DatabaseException {
        List<Tournament> tournaments = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            List<Team> teams = getTeamsFromDatabase();
            List<Result> results = getAllResultsFromDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TOURNAMENT");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                String venue = resultSet.getString("venue");
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM TOURNAMENT_TEAM WHERE TOURNAMENT_ID = " + id);
                List<Team> tournamentTeams = new ArrayList<>();
                while (resultSet2.next()) {
                    Integer teamID = resultSet2.getInt("team_id");
                    Optional<Team> team = teams.stream()
                            .filter(t->t.getId().equals(teamID))
                            .findFirst();
                    tournamentTeams.add(team.get());
                }
                Statement statement3 = connection.createStatement();
                ResultSet resultSet3 = statement3.executeQuery("SELECT * FROM RESULT WHERE TOURNAMENT_ID = " + id);
                List<Result> tournamentResults = new ArrayList<>();
                while (resultSet3.next()) {
                    Integer ID = resultSet3.getInt("id");
                    Integer teamOneId = resultSet3.getInt("team_one_id");
                    Integer teamTwoId = resultSet3.getInt("team_two_id");
                    Integer teamOneWins = resultSet3.getInt("team_one_wins");
                    Integer teamTwoWins = resultSet3.getInt("team_two_wins");
                    String bracketStage = resultSet3.getString("bracket_type");
                    Optional<Team> one = teams.stream()
                            .filter(t->t.getId().equals(teamOneId))
                            .findAny();
                    Optional<Team> two = teams.stream()
                            .filter(t->t.getId().equals(teamTwoId))
                            .findAny();
                    tournamentResults.add(new Result(one.get(), two.get(), teamOneWins.toString(), teamTwoWins.toString(), bracketStage, ID));
                }
                /*while (resultSet3.next()) {
                    Integer resultID = resultSet3.getInt("result_id");
                    Optional<Result> result = results.stream()
                            .filter(r -> r.id().equals(resultID))
                            .findFirst();
                    tournamentResults.add(result.get());
                }*/
                tournaments.add(new Tournament(id, name, location, venue, startDate, endDate, tournamentTeams, tournamentResults));
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while reading tournament database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
        return tournaments;
    }
    public static void saveNewTournamentToDatabase(Tournament tournament) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TOURNAMENT(name, location, venue, start_date, end_date) VALUES (?, ?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO TOURNAMENT_TEAM(tournament_id, team_id) VALUES (?, ?)");
            PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO RESULT(team_one_id, team_two_id, team_one_wins, team_two_wins, BRACKET_TYPE, TOURNAMENT_ID) VALUES (?, ?, ? ,?, ?, ?)");
            preparedStatement.setString(1, tournament.getName());
            preparedStatement.setString(2, tournament.getLocation());
            preparedStatement.setString(3, tournament.getVenue());
            preparedStatement.setString(4, tournament.getStartDate().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, tournament.getEndDate().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int tournamentId;
            if (generatedKeys.next()) {
                tournamentId = generatedKeys.getInt(1);
            } else {
                throw new DatabaseException("Failed to retrieve the generated tournament ID.");
            }

            for (Team t : tournament.getTeams()){
                preparedStatement2.setInt(1, tournamentId);
                preparedStatement2.setInt(2, t.getId());
                preparedStatement2.executeUpdate();
            }
            for (Result r : tournament.getResults()){
                preparedStatement3.setInt(1, r.teamOne().getId());
                preparedStatement3.setInt(2, r.teamTwo().getId());
                preparedStatement3.setInt(3, Integer.parseInt(r.teamOneWins()));
                preparedStatement3.setInt(4, Integer.parseInt(r.teamTwoWins()));
                preparedStatement3.setString(5, r.bracketStage());
                preparedStatement3.setInt(6, tournamentId);
                preparedStatement3.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while adding tournament to database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void editTournamentToDatabase(Tournament tournament) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement5 = connection.prepareStatement("DELETE FROM RESULT WHERE TOURNAMENT_ID = " + tournament.getId());
            PreparedStatement preparedStatement6 = connection.prepareStatement("DELETE FROM TOURNAMENT_TEAM WHERE TOURNAMENT_ID = " + tournament.getId());
            preparedStatement6.executeUpdate();
            preparedStatement5.executeUpdate();

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TOURNAMENT SET name = ?, location = ?, venue = ?, start_date = ?, end_date = ? WHERE ID = " + tournament.getId());
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO TOURNAMENT_TEAM(tournament_id, team_id) VALUES (?, ?)");
            PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO RESULT (team_one_id, team_two_id, team_one_wins, team_two_wins, BRACKET_TYPE, TOURNAMENT_ID) VALUES (?,?,?,?,?,?)");

            preparedStatement.setString(1, tournament.getName());
            preparedStatement.setString(2, tournament.getLocation());
            preparedStatement.setString(3, tournament.getVenue());
            preparedStatement.setString(4, tournament.getStartDate().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, tournament.getEndDate().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.executeUpdate();

            for (Team t : tournament.getTeams()){
                preparedStatement2.setInt(1, tournament.getId());
                preparedStatement2.setInt(2, t.getId());
                preparedStatement2.executeUpdate();
            }
            for (Result r : tournament.getResults()){
                preparedStatement3.setInt(1, r.teamOne().getId());
                preparedStatement3.setInt(2, r.teamTwo().getId());
                preparedStatement3.setInt(3, Integer.parseInt(r.teamOneWins()));
                preparedStatement3.setInt(4, Integer.parseInt(r.teamTwoWins()));
                preparedStatement3.setString(5, r.bracketStage());
                preparedStatement3.setInt(6, tournament.getId());
                preparedStatement3.executeUpdate();
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while adding tournament to database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void deleteTournamentFromDatabase(Tournament tournament) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM TOURNAMENT_TEAM WHERE TOURNAMENT_ID = " + tournament.getId());
            PreparedStatement preparedStatement4 = connection.prepareStatement("DELETE FROM TOURNAMENT WHERE ID = " + tournament.getId());
            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM RESULT WHERE TOURNAMENT_ID = " + tournament.getId());

            preparedStatement2.executeUpdate();
            preparedStatement4.executeUpdate();
            preparedStatement3.executeUpdate();
        } catch (SQLException | IOException ex) {
            String msg = "Error while deleting tournament from database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void deleteTeamFromDatabase(Team team) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM TEAM_PLAYER WHERE TEAM_ID = " + team.getId());
            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM TEAM WHERE ID = " + team.getId());
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
        } catch (SQLException | IOException ex) {
            String msg = "Error while deleting tournament from database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static void deletePlayerFromDatabase(Player player) throws DatabaseException {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM PLAYER WHERE ID = " + player.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String msg = "Error while deleting tournament from database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
    }
    public static List<Result> getAllResultsFromDatabase() throws DatabaseException {
        List<Result> results = new ArrayList<>();
        List<Team> teams = getTeamsFromDatabase();
        try (Connection connection = connectToDatabase()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM RESULT");
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer teamOneId = resultSet.getInt("team_one_id");
                Integer teamTwoId = resultSet.getInt("team_two_id");
                Integer teamOneWins = resultSet.getInt("team_one_wins");
                Integer teamTwoWins = resultSet.getInt("team_two_wins");
                String bracketStage = resultSet.getString("bracket_type");
                Optional<Team> one = teams.stream()
                        .filter(t->t.getId().equals(teamOneId))
                                .findAny();
                Optional<Team> two = teams.stream()
                        .filter(t->t.getId().equals(teamTwoId))
                        .findAny();
                results.add(new Result(one.get(), two.get(), teamOneWins.toString(), teamTwoWins.toString(), bracketStage, id));
            }
        } catch (SQLException | IOException ex) {
            String msg = "Error while accessing players database!";
            logger.error(msg, ex);
            throw new DatabaseException(msg, ex);
        }
        return results;
    }
}
