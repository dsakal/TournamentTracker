package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseInfo extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2864836724017662807L;
    private String name;

    public BaseInfo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
