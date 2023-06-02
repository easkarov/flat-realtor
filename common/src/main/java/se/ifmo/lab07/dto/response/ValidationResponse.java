package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.Credentials;
import se.ifmo.lab07.dto.StatusCode;

public record ValidationResponse(
        String message,
        StatusCode status,
        Credentials credentials
) implements Response {
    public ValidationResponse(String message) {
        this(message, StatusCode.OK);
    }

    public ValidationResponse(String message, StatusCode status) {
        this(message, status, null);
    }
}
