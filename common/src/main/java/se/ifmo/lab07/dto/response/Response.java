package se.ifmo.lab07.dto.response;

import java.io.Serializable;

public sealed interface Response extends Serializable permits CommandResponse, GetInfoResponse, ValidationResponse,
        PingResponse {
}
