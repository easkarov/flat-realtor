package se.ifmo.lab07.model;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private long x; //Значение поля должно быть больше -952
    private Float y; //Максимальное значение поля: 779, Поле не может быть null

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