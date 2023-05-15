package se.ifmo.lab07.util;

public class CLIPrinter implements Printer {
    @Override
    public void print(String text) {
        System.out.println(text);
    }

    @Override
    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

}
