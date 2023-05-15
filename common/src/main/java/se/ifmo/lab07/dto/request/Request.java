package se.ifmo.lab07.dto.request;


import java.io.Serializable;

public sealed interface Request extends Serializable permits ValidationRequest, CommandRequest, GetCommandsRequest, PingRequest {
}
