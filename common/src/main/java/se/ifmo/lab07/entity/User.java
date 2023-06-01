package se.ifmo.lab07.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class User implements Serializable {

    private Integer id;
    private final String username;
    private String password;
    private String salt;

    public User(String username, String password, String salt) {
        this(null, username, password, salt);
    }

    public User(Integer id, String username, String password, String salt) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.id = id;
    }

    @Override
    public String toString() {
        return "User(id=%s, username=%s)".formatted(id, username);
    }

}

