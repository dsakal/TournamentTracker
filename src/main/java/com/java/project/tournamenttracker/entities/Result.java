package com.java.project.tournamenttracker.entities;

import java.io.Serializable;

public record Result(Team teamOne, Team teamTwo, String teamOneWins, String teamTwoWins, String bracketStage, Integer id) implements Serializable {
    public Result(Team teamOne, Team teamTwo, String teamOneWins, String teamTwoWins, String bracketStage, Integer id) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.teamOneWins = teamOneWins;
        this.teamTwoWins = teamTwoWins;
        this.bracketStage = bracketStage;
        this.id = id;
    }

    @Override
    public Team teamOne() {
        return teamOne;
    }

    @Override
    public Team teamTwo() {
        return teamTwo;
    }

    @Override
    public String teamOneWins() {
        return teamOneWins;
    }

    @Override
    public String teamTwoWins() {
        return teamTwoWins;
    }

    @Override
    public String bracketStage() {
        return bracketStage;
    }
    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String toString() {
        String teamOneString = teamOne.toString().replace("[", "").replace("]", "");
        String teamTwoString = teamTwo.toString().replace("[", "").replace("]", "");

        return bracketStage + ": " + teamOneString.replaceAll(",", "")
                + " " + teamOneWins + " : " + teamTwoString.replaceAll(",", "")
                + ": " + teamTwoWins + "\n";
    }

}
