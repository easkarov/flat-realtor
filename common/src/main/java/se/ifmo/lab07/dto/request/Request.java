package se.ifmo.lab07.dto.request;


import java.io.Serializable;

public sealed interface Request extends Serializable permits AuthRequest, CommandRequest, GetCommandsRequest, PingRequest, RegisterRequest, ValidationRequest {
}
