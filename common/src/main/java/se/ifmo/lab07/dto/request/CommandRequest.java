package se.ifmo.lab07.dto.request;


import se.ifmo.lab07.entity.Flat;

import java.io.Serializable;

public final class CommandRequest extends Request implements Serializable {
    private final String name;
    private Flat model;

    public CommandRequest(String name, String... args) {
        super(args);
        this.name = name;
    }

    public void setModel(Flat flat) {
        this.model = flat;
    }

    public CommandRequest(String name) {
        this(name, new String[]{});
    }

    public String name() {
        return name;
    }

    public Flat model() {
        return model;
    }
}
