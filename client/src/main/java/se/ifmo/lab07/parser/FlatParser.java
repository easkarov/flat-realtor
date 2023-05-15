package se.ifmo.lab07.parser;

import se.ifmo.lab07.util.Printer;
import se.ifmo.lab07.model.*;

import java.util.Arrays;
import java.util.Scanner;

public class FlatParser extends DefaultParser {

    public FlatParser(Scanner scanner, Printer printer) {
        super(scanner, printer);
    }

    public House parseHouse() {
        print("HOUSE:");
        // House Name
        String name;
        while (!House.validateName(name = parseString("Name", "not null"))) print("Invalid Name.");
        // House year
        Long year;
        while (!House.validateYear(year = parseLong("Year", "long, max 636, min 1"))) print("Invalid Year.");
        // House numberOfFlatsOnFloor
        Integer flatsNumber;
        while (!House.validateFlatsNumber(flatsNumber = parseInt("Number of Flats on Floor", "not null, int, min 1"))) {
            print("Invalid Number of Flats on Floor.");
        }
        return new House(name, year, flatsNumber);
    }

    public Coordinates parseCoordinates() {
        print("COORDINATES:");
        // Coordinate X
        Long x;
        while (!Coordinates.validateX(x = parseLong("X", "not null, long, min -951"))) print("Invalid X.");
        // Coordinate Y
        Float y;
        while (!Coordinates.validateY(y = parseFloat("Y", "not null, float, max 779"))) print("Invalid Y.");
        return new Coordinates(x, y);
    }

    public Flat parseFlat() {
        print("FLAT:");
        // Flat Name
        String name;
        while (!Flat.validateName(name = parseString("Name", "not null, not empty"))) print("Invalid Name.");
        // Flat Coordinates
        Coordinates coordinates = parseCoordinates();
        // Flat Area
        Long area;
        while (!Flat.validateArea(area = parseLong("Area", "not null, long, max 715, min 1"))) print("Invalid Area.");
        // Flat NumberOfRooms
        Long numberOfRooms;
        while (!Flat.validateNumberOfRooms(numberOfRooms = parseLong("Number of Rooms", "long, min 1"))) {
            print("Invalid Number of Rooms");
        }
        // Flat Furnish
        Furnish furnish;
        String furnishValues = Arrays.asList(Furnish.values()).toString();
        while (!Flat.validateFurnish(furnish = parseEnum(Furnish.class, "Furnish " + furnishValues, ""))) {
            print("Invalid Furnish " + furnishValues);
        }
        // Flat View
        View view;
        String viewValues = Arrays.asList(View.values()).toString();
        while (!Flat.validateView(view = parseEnum(View.class, "View " + viewValues, "not null"))) {
            print("Invalid View " + viewValues);
        }
        // Flat Transport
        Transport transport;
        String transportValues = Arrays.asList(Transport.values()).toString();
        while (!Flat.validateTransport(transport = parseEnum(Transport.class, "Transport " + transportValues, ""))) {
            print("Invalid Transport " + transportValues);
        }
        // Flat House
        House house = parseHouse();

        return new Flat(name, coordinates, area, numberOfRooms, furnish, view, transport, house);
    }
}
