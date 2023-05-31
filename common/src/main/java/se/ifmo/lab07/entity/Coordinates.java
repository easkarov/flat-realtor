package se.ifmo.lab07.entity;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class Coordinates {
    private long x;
    private Float y;

    public Coordinates(long x, Float y) {
        this.x = x;
        this.y = y;
    }

    public static boolean validateX(Long x) {
        return (x != null && x > -952L);
    }

    public static boolean validateY(Float y) {
        return (y != null && y <= 779F);
    }

    public boolean validate() {
        return (validateX(this.x) && validateY(this.y));
    }

    public Coordinates update(Coordinates coordinates) {
        this.x = coordinates.x;
        this.y = coordinates.y;
        return this;
    }

    @Override
    public String toString() {
        return String.format("---- X: %s\n---- Y: %s", x, y);
    }
}