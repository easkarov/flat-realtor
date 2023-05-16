package se.ifmo.lab07;

import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.parser.CommandParser;
import se.ifmo.lab07.util.CLIPrinter;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.util.Printer;

import java.util.Properties;
import java.util.Scanner;


public class Main {

    private static final String PROPS = "client.properties";

    public static void main(String[] args) {
            try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPS)) {
            var props = new Properties();
            props.load(stream);
            var host = props.getProperty("HOST");
            var port = Integer.parseInt(props.getProperty("PORT"));
            var maxRecDepth = Integer.parseInt(props.getProperty("MAX_REC_DEPTH"));

            Scanner scanner = new Scanner(System.in);
            Printer printer = new CLIPrinter();
            IOProvider provider = new IOProvider(scanner, printer);

            try (var client = Client.connect(host, port)) {
                var manager = new CommandManager(client, provider, 0);
                var parser = new CommandParser(client, manager, provider, 0);
                CommandParser.setMaxRecDepth(maxRecDepth);
                parser.run();
            }
        } catch (Exception e) {
            System.out.printf("Error occurred:\n%s", e.getMessage());
            System.exit(-1);
        }
    }
}