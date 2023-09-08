package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Tournament extends  BaseInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -8375716737379869374L;
    private String location;
    private String venue;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Team> teams;
    private List<Result> results;

    public Tournament(Integer id, String name, String location, String venue, LocalDate startDate, LocalDate endDate, List<Team> teams, List<Result> results) {
        super(id, name);
        this.location = location;
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teams = teams;
        this.results = results;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        String teamsString = teams.toString().replace("[", "").replace("]", "");
        String resultsString = results.toString().replace("[", "").replace("]", "").replaceAll(",", "");

        return "Name: " + getName() + "\n" +
               "Location: " + location + "\n" +
               "Venue: " + venue + "\n" +
               "Date: " + startDate + " - " + endDate + "\n" +
               "Teams: " + teamsString + "\n" +
               "Results: " + "\n" + resultsString;
    }

}
