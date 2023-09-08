package com.java.project.tournamenttracker.entities;

public enum Role {
    ADMIN(1, "ADMIN"),
    USER(2, "USER"),
    GUEST(3, "GUEST");

    private final Integer clearanceLevel;
    private final String role;

    Role(Integer clearanceLevel, String role) {
        this.clearanceLevel = clearanceLevel;
        this.role = role;
    }

    public Integer getClearanceLevel() {
        return clearanceLevel;
    }

    public String getRole() {
        return role;
    }
}
