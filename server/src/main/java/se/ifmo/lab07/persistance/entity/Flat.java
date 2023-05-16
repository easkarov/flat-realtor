package se.ifmo.lab07.persistance.entity;

import java.time.ZonedDateTime;

public record Flat(
        long id,
        String name,
        Coordinates coordinates,
        ZonedDateTime createdAt,
        long area,
        Long numberOfRooms,
        Furnish furnish,
        View view,
        Transport transport,
        House house,
        User owner
) implements Comparable<Flat> {

    @Override
    public String toString() {
        return String.format("ID: %s\nName: %s\nCoordinates:\n%s\nCreation date: %s\nArea: %s\nNumber Of Rooms: %s\n" +
                        "Furnish: %s\nView: %s\nTransport: %s\nHouse:\n%s",
                id, name, coordinates, createdAt, area, numberOfRooms, furnish, view, transport, house);
    }

    @Override
    public int compareTo(Flat flat) {
        return Long.compare(this.area(), flat.area());
    }

}