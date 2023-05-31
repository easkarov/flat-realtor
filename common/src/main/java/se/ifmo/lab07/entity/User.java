package se.ifmo.lab07.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(fluent = true)
public class User {

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
}

