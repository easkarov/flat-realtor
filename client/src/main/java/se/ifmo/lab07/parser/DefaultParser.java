package se.ifmo.lab07.parser;

import se.ifmo.lab07.exception.InterruptCommandException;
import se.ifmo.lab07.util.Printer;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class DefaultParser implements Parser {
    private final Scanner input;
    private final Printer printer;

    public DefaultParser(Scanner scanner, Printer printer) {
        this.input = scanner;
        this.printer = printer;
    }

    public String parseString(String name, String descr) throws NoSuchElementException {
        if (descr == null || descr.equals("")) {
            printer.printf("\nEnter %s:\n", name);
        } else {
            printer.printf("\nEnter %s (%s):\n", name, descr);
        }
        String line = input.nextLine().strip();

        if (line.equals("exit")) {
            throw new InterruptCommandException();
        }
        return line.equals("") ? null : line;
    }

    public Integer parseInt(String name, String descr) {
        while (true) {
            try {
                String line = parseString(name, descr);
                return (line != null) ? Integer.parseInt(line) : null;
            } catch (NumberFormatException exception) {
                printer.printf("Invalid %s.%n", name);
            }
        }
    }

    public Long parseLong(String name, String descr) {
        while (true) {
            try {
                String line = parseString(name, descr);
                return (line != null) ? Long.parseLong(line) : null;
            } catch (NumberFormatException exception) {
                printer.printf("Invalid %s.%n", name);
            }
        }
    }

    public Float parseFloat(String name, String descr) {
        while (true) {
            try {
                String line = parseString(name, descr);
                return (line != null) ? Float.parseFloat(line) : null;
            } catch (NumberFormatException exception) {
                printer.printf("Invalid %s.%n", name);
            }
        }
    }

    public Double parseDouble(String name, String descr) {
        while (true) {
            try {
                String line = parseString(name, descr);
                return (line != null) ? Double.parseDouble(line) : null;
            } catch (NumberFormatException exception) {
                printer.printf("Invalid %s.%n", name);
            }
        }
    }

    public <T extends Enum<T>> T parseEnum(Class<T> enumType, String name, String descr) {
        while (true) {
            try {
                String line = parseString(name, descr);
                return (line != null) ? Enum.valueOf(enumType, line) : null;
            } catch (NullPointerException | IllegalArgumentException exception) {
                printer.printf("Invalid %s.%n", name);
            }
        }
    }

    public void print(String text) {
        printer.print(text);
    }

}
