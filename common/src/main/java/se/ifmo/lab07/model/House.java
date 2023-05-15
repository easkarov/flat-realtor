package se.ifmo.lab07.model;

import java.io.Serializable;
import java.util.Objects;

public class House implements Serializable {
    private String name; //Поле не может быть null
    private Long year; //Максимальное значение поля: 636, Значение поля должно быть больше 0
    private int numberOfFlatsOnFloor; //Значение поля должно быть больше 0

    public House(String name, Long year, int numberOfFlatsOnFloor) {
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    public static boolean validateName(String name) {
        return (name != null);
    }

    public static boolean validateYear(Long year) {
        return (year == null || (year > 0 && year <= 636));
    }

    public static boolean validateFlatsNumber(Integer number) {
        return (number != null && number > 0);
    }

    public boolean validate() {
        return (validateName(this.name) && validateYear(this.year) && validateFlatsNumber(this.numberOfFlatsOnFloor));
    }

    public House update(House house) {
        this.name = house.name;
        this.year = house.year;
        this.numberOfFlatsOnFloor = house.numberOfFlatsOnFloor;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.year, this.numberOfFlatsOnFloor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return numberOfFlatsOnFloor == house.numberOfFlatsOnFloor &&
                name.equals(house.name) &&
                year.equals(house.year);
    }

    @Override
    public String toString() {
        return String.format("---- Name: %s\n---- Year: %s\n---- Number of Flats On Floor: %s",
                name, year, numberOfFlatsOnFloor);
    }
}
