package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.Credentials;
import se.ifmo.lab07.dto.StatusCode;

public record CommandResponse(String message, StatusCode status, Credentials credentials) implements Response {
    public CommandResponse(String message) {
        this(message, StatusCode.OK);
    }

    public CommandResponse(String message, StatusCode status) {
        this(message, status, null);
    }
}
