package se.ifmo.lab07.dto.request;

public final class ValidationRequest extends Request {
    private final String name;

    public ValidationRequest(String name, String[] args) {
        super(args);
        this.name = name;
    }

    public String name() {
        return name;
    }
}