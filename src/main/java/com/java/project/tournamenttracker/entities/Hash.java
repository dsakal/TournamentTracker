package com.java.project.tournamenttracker.entities;


public sealed interface Hash permits User{
    String hashPassword(String passwordToHash);
}
