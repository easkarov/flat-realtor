package se.ifmo.lab07.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import se.ifmo.lab07.dto.Role;

import java.io.Serializable;


@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class User implements Serializable {

    private Integer id;
    private final String username;
    private String password;
    private String salt;
    private Role role;

    public User(String username, String password, String salt) {
        this(null, username, password, salt, Role.MIN_USER);
    }

    public User(Integer id, String username, String password, String salt, Role role) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.id = id;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User(id=%s, username=%s)".formatted(id, username);
    }

}

