package se.ifmo.lab07;

import se.ifmo.lab07.exception.ExitException;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.parser.CommandParser;
import se.ifmo.lab07.util.CLIPrinter;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.util.Printer;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Printer printer = new CLIPrinter();
            IOProvider provider = new IOProvider(scanner, printer);

            try (var client = Client.connect(Configuration.HOST, Configuration.PORT)) {
                var manager = new CommandManager(client, provider, 0);
                var parser = new CommandParser(client, manager, provider, 0);
                parser.run();
            }
        } catch (ExitException e) {
            System.out.println("Program has finished. Good luck!");
        } catch (Exception e) {
            System.out.printf("Error occurred:\n%s", e.getMessage());
            System.exit(-1);
        }
    }
}