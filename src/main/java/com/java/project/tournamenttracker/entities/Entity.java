package com.java.project.tournamenttracker.entities;

import java.io.Serial;
import java.io.Serializable;

public class Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 6946630041867794247L;
    private Integer id;

    public Entity(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
