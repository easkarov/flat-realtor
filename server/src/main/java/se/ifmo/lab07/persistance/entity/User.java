package se.ifmo.lab07.persistance.entity;

public record User(Integer id, String username, String password, String salt) {
    public User(String username, String password, String salt) {
        this(null, username, password, salt);
    }
}
