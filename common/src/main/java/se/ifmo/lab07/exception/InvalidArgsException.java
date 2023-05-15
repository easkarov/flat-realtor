package se.ifmo.lab07.exception;

public class InvalidArgsException extends Exception {
    public InvalidArgsException(String message) {
        super(message);
    }

    public InvalidArgsException() {
        super("Invalid arguments");
    }
}
