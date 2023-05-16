package se.ifmo.lab07.persistance.entity;

public record Coordinates(
        long x,
        Float y
) {
    @Override
    public String toString() {
        return String.format("---- X: %s\n---- Y: %s", x, y);
    }
}