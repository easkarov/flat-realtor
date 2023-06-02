package se.ifmo.lab07.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super("Access denied. Unauthorized. %s".formatted(message));
    }

    public AuthorizationException() {
        this("");
    }
}
