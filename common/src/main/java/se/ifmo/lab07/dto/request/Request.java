package se.ifmo.lab07.dto.request;


import java.io.Serializable;

public sealed abstract class Request implements Serializable permits CommandRequest, GetCommandsRequest, PingRequest, ValidationRequest {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }

}
