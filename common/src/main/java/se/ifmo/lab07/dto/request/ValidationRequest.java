package se.ifmo.lab07.dto.request;

public record ValidationRequest(
        String commandName,
        String[] args
) implements Request {
}
