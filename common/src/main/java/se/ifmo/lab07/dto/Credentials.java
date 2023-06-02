package se.ifmo.lab07.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class Credentials implements Serializable {
    private String username;
    private String password;
    private Role role;

    public Credentials(String username, String password) {
        this(username, password, Role.MIN_USER);
    }

    public Credentials(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
