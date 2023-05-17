package se.ifmo.lab07.dto.request;

public record RegisterRequest(String username, String password) implements Request {
}
