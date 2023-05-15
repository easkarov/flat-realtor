package se.ifmo.lab07.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Flat implements Comparable<Flat>, Serializable {
    private final long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    public ZonedDateTime creationDate = ZonedDateTime.now(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long area; //Максимальное значение поля: 715, Значение поля должно быть больше 0
    private Long numberOfRooms; //Значение поля должно быть больше 0
    private Furnish furnish; //Поле может быть null
    private View view; //Поле не может быть null
    private Transport transport; //Поле может быть null
    private House house; //Поле не может быть null

    public Flat(String name, Coordinates coordinates, long area, Long numberOfRooms,
                 Furnish furnish, View view, Transport transport, House house) {
        this.id = Math.abs(UUID.randomUUID().hashCode());
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getArea() {
        return area;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public House getHouse() {
        return house;
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

    public static boolean validateHouse(House house)     {
        return (house != null && house.validate());
    }

    public boolean validate() {
        return (validateId(this.id) &&
                validateName(this.name) &&
                validateCoordinates(this.coordinates) &&
                validateCreationDate(this.creationDate) &&
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
                id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);
    }

    @Override
    public int compareTo(Flat flat) {
        return Long.compare(this.getArea(), flat.getArea());
    }
}