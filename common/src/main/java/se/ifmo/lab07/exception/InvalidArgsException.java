package se.ifmo.lab07.exception;

public class InvalidArgsException extends RuntimeException {
    public InvalidArgsException(String message) {
        super(message);
    }

    public InvalidArgsException() {
        super("Invalid arguments");
    }
}
