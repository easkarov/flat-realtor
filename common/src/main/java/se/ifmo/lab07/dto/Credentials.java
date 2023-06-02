package se.ifmo.lab07.dto;

import java.io.Serializable;

public record Credentials(String username, String password, Role role) implements Serializable {
    public Credentials(String username, String password) {
        this(username, password, Role.MIN_USER);
    }
}
