package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Team extends BaseInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 7775953019172372246L;
    private List<Player> players;
    private String location;
    private LocalDate dateFounded;

    public Team(Integer id, String name, List<Player> players, String location, LocalDate dateFounded) {
        super(id, name);
        this.players = players;
        this.location = location;
        this.dateFounded = dateFounded;
    }
    public Team(Integer id, String name) {
        super(id, name);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDateFounded() {
        return dateFounded;
    }

    public void setDateFounded(LocalDate dateFounded) {
        this.dateFounded = dateFounded;
    }

    @Override
    public String toString() {
        return getName();
    }
}
