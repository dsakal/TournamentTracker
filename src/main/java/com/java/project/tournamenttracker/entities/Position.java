package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;

public enum Position implements Serializable {
    CARRY(1, "Carry"),
    MID(2, "Middle"),
    OFFLANE(3, "Offlane"),
    SOFT_SUPPORT(4, "Soft support"),
    HARD_SUPPORT(5, "Hard support");

    private final Integer numericalValue;
    private final String description;

    private static final long serialVersionUID = 1L;
    Position(Integer numericalValue, String description) {
        this.numericalValue = numericalValue;
        this.description = description;
    }

    public Integer getNumericalValue() {
        return numericalValue;
    }

    public String getDescription() {
        return description;
    }
}
