package se.ifmo.lab07.dto.response;

import se.ifmo.lab07.dto.CommandDTO;
import se.ifmo.lab07.dto.StatusCode;

import java.util.List;

public record GetCommandsResponse(
        List<CommandDTO> commands,
        StatusCode status,
        String token
) implements Response {
    public GetCommandsResponse(List<CommandDTO> commands) {
        this(commands, StatusCode.OK);
    }

    public GetCommandsResponse(List<CommandDTO> commands, StatusCode status) {
        this(commands, status, null);
    }
}
