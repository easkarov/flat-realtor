package se.ifmo.lab07.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;


@Data
@Accessors(fluent = true, chain = true)
public class House implements Serializable {
    private Integer id;
    private String name;
    private Long year;
    private int numberOfFlatsOnFloor;

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

}