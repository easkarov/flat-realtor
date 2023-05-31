package se.ifmo.lab07.dto.request;

public final class ValidationRequest extends Request {
    private final String name;
    private final String[] args;

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