package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Player extends BaseInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -6211935734557668397L;
    private String nickname;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private Position position;
    private String image;
    public Player(Integer id, String name, String nickname, String lastName, LocalDate dateOfBirth, String nationality, Position position, String image) {
        super(id, name);
        this.nickname = nickname;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.position = position;
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /*@Override
    public String toString() {
        return nickname;
    }*/

    @Override
    public String toString() {
        return getName() + " '" + nickname + "' " + lastName + " " + dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")) + " " + nationality + " " + position + "\n";
    }
}
