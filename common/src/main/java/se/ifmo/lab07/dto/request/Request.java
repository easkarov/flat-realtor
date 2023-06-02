package se.ifmo.lab07.dto.request;


import se.ifmo.lab07.dto.Credentials;

import java.io.Serializable;

public sealed abstract class Request implements Serializable permits CommandRequest, GetCommandsRequest, PingRequest, ValidationRequest {
    private Credentials credentials;
    private final String[] args;

    public Request(String[] args) {
        this.args = args;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials credentials() {
        return credentials;
    }

    public String[] args() {
        return args;
    }
}
