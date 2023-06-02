package se.ifmo.lab07.dto;

import java.io.Serializable;

public enum Role implements Serializable {

    MIN_USER(1),
    MIDDLE_USER(2),
    ADMIN(3);

    private final int weight;

    Role(int weight) {
        this.weight = weight;
    }

    public int weight() {
        return weight;
    }
}
