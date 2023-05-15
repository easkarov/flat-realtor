package se.ifmo.lab07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.network.Server;
import se.ifmo.lab07.util.CLIPrinter;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.util.Printer;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {

        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server.properties")) {
            Properties props = new Properties();
            props.load(stream);
            var filename = props.getProperty("FILENAME");
            var port = Integer.parseInt(props.getProperty("PORT"));

            Scanner scanner = new Scanner(System.in);
            Printer printer = new CLIPrinter();
            IOProvider provider = new IOProvider(scanner, printer);

            CollectionManager collectionManager = CollectionManager.fromFile(filename);
            CommandManager commandManager = new CommandManager(collectionManager, provider);

            try (var server = new Server(commandManager, port)) {
                server.run();
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found or access denied (read):\n{}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error. Something went wrong:\n{}", e.getMessage());
        }
    }
}

