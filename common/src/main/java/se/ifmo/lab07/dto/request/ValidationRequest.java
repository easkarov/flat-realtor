package se.ifmo.lab07.dto.request;

public final class ValidationRequest extends Request {
    private String name;
    private String[] args;

    public ValidationRequest(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    public String name() {
        return name;
    }

    public String[] args() {
        return args;
    }
}