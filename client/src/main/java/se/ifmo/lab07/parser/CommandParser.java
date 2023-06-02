package se.ifmo.lab07.parser;

import se.ifmo.lab07.dto.request.GetInfoRequest;
import se.ifmo.lab07.dto.response.GetInfoResponse;
import se.ifmo.lab07.exception.*;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.util.Printer;

import javax.naming.TimeLimitExceededException;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandParser extends DefaultParser {
    private final CommandManager commandManager;
    private final IOProvider provider;
    private final Client client;
    private static int maxRecDepth = 5;
    private final int recDepth;

    public CommandParser(Client client, CommandManager commandManager, IOProvider provider, int recDepth) {
        super(provider.getScanner(), provider.getPrinter());
        this.commandManager = commandManager;
        this.provider = provider;
        this.recDepth = recDepth;
        this.client = client;
    }

    public static void setMaxRecDepth(int recDepth) {
        maxRecDepth = recDepth;
    }

    public void run() {
        Scanner scanner = provider.getScanner();
        Printer printer = provider.getPrinter();
        if (recDepth > maxRecDepth) {
            throw new RecursionException();
        }
        while (true) {
            try {
                printer.printf("\nEnter command:\n");
                String line = scanner.nextLine();
                String[] splitLine = line.strip().split("\s+");
                String commandName = splitLine[0].toLowerCase();
                String[] args = Arrays.copyOfRange(splitLine, 1, splitLine.length);

                commandManager.getServerCommands().clear();
                var response = (GetInfoResponse) client.sendAndReceive(new GetInfoRequest());
                commandManager.register(response.commands());
                client.setCredentials(response.credentials());

                if (commandManager.getClientCommand(commandName).isPresent()) {
                    commandManager.executeClientCommand(commandName, args);
                }
                var serverCommand = commandManager.getServerCommand(commandName);
                if (serverCommand.isEmpty()) {
                    printer.print("Invalid command");
                    continue;
                }
                commandManager.executeServerCommand(commandName, args);
            } catch (InterruptCommandException e) {
                printer.print("\nExited\n");
            } catch (NoSuchElementException e) {
                printer.print("EOF");
                break;
            } catch (InvalidArgsException | AuthorizationException | RoleException | TimeLimitExceededException e) {
                printer.print(e.getMessage());
            } catch (RecursionException e) {
                printer.print("Recursion depth exceeded!");
                break;
            } catch (IOException e) {
                provider.getPrinter().printf("Error occurred while I/O\n%s\n", e.toString());
            } catch (ClassNotFoundException e) {
                provider.getPrinter().print("Invalid response format from server");
            }
        }
    }
}


