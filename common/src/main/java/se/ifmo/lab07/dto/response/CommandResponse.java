package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.StatusCode;

public record CommandResponse(String message, StatusCode status) implements Response {
    public CommandResponse(String message) {
        this(message, StatusCode.OK);
    }
}
