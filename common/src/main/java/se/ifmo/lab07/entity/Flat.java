package se.ifmo.lab07.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;


@Data
@Accessors(fluent = true, chain = true)
public class Flat implements Comparable<Flat> {
    private long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime createdAt;
    private long area;
    private Long numberOfRooms;
    private Furnish furnish;
    private View view;
    private Transport transport;
    private House house;
    private User owner;

    @Override
    public int compareTo(Flat flat) {
        return Long.compare(this.area(), flat.area());
    }

    public static boolean validateId(Long id) {
        return (id != null && id > 0);
    }

    public static boolean validateName(String name) {
        return name != null;
    }

    public static boolean validateCoordinates(Coordinates coordinates) {
        return (coordinates != null && coordinates.validate());
    }

    public static boolean validateCreationDate(ZonedDateTime creationDate) {
        return (creationDate != null);
    }

    public static boolean validateArea(Long area) {
        return (area != null && area <= 715L && area > 0L);
    }

    public static boolean validateNumberOfRooms(Long numberOfRooms) {
        return (numberOfRooms == null || numberOfRooms > 0);
    }

    public static boolean validateFurnish(Furnish furnish) {
        return true;
    }

    public static boolean validateView(View view) {
        return (view != null);
    }

    public static boolean validateTransport(Transport transport) {
        return true;
    }

    public static boolean validateHouse(House house) {
        return (house != null && house.validate());
    }

    public boolean validate() {
        return (validateId(this.id) &&
                validateName(this.name) &&
                validateCoordinates(this.coordinates) &&
                validateCreationDate(this.createdAt) &&
                validateArea(this.area) &&
                validateNumberOfRooms(this.numberOfRooms) &&
                validateView(this.view) &&
                validateHouse(this.house));
    }

    public Flat update(Flat flat) {
        this.name = flat.name;
        this.coordinates = this.coordinates.update(flat.coordinates);
        this.area = flat.area;
        this.numberOfRooms = flat.numberOfRooms;
        this.furnish = flat.furnish;
        this.view = flat.view;
        this.transport = flat.transport;
        this.house = this.house.update(flat.house);
        return this;
    }

    @Override
    public String toString() {
        return String.format("ID: %s\nName: %s\nCoordinates:\n%s\nCreation date: %s\nArea: %s\nNumber Of Rooms: %s\n" +
                        "Furnish: %s\nView: %s\nTransport: %s\nHouse:\n%s",
                id, name, coordinates, createdAt, area, numberOfRooms, furnish, view, transport, house);
    }
}