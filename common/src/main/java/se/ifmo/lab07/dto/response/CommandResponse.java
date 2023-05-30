package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.StatusCode;

public record CommandResponse(String message, StatusCode status, String token) implements Response {
    public CommandResponse(String message) {
        this(message, StatusCode.OK);
    }

    public CommandResponse(String message, StatusCode status) {
        this(message, status, null);
    }
}
