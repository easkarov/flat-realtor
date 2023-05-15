package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.StatusCode;

public record ValidationResponse(
        String message,
        StatusCode status
) implements Response {
    public ValidationResponse(String message) {
        this(message, StatusCode.OK);
    }
}
