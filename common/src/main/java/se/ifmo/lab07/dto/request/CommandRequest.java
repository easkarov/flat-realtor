package se.ifmo.lab07.dto.request;


import se.ifmo.lab07.model.Flat;

import java.io.Serializable;

public final class CommandRequest implements Request, Serializable {
    private final String name;
    private final String[] args;
    private Flat model;

    public CommandRequest(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    public void setModel(Flat flat) {
        this.model = flat;
    }

    public CommandRequest(String name) {
        this(name, new String[]{});
    }

    public String[] args() {
        return args;
    }

    public String name() {
        return name;
    }

    public Flat model() {
        return model;
    }
}
