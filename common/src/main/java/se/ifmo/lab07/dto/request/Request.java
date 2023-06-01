package se.ifmo.lab07.dto.request;


import java.io.Serializable;

public sealed abstract class Request implements Serializable permits CommandRequest, GetCommandsRequest, PingRequest, ValidationRequest {
    private String token;
    private final String[] args;

    public Request(String[] args) {
        this.args = args;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token() {
        return token;
    }

    public String[] args() {
        return args;
    }
}
