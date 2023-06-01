package se.ifmo.lab07.parser;

import se.ifmo.lab07.command.Authorized;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.dto.request.GetCommandsRequest;
import se.ifmo.lab07.dto.response.GetCommandsResponse;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.util.Printer;
import se.ifmo.lab07.util.ArgumentValidator;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.ValidationRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.ValidationResponse;
import se.ifmo.lab07.exception.ExitException;
import se.ifmo.lab07.exception.InterruptCommandException;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.exception.RecursionException;

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
                var response = client.sendAndReceive(new GetCommandsRequest());
                commandManager.register(((GetCommandsResponse) response).commands());

                var clientCommand = commandManager.getClientCommand(commandName);
                if (clientCommand.isPresent() && clientCommand.get() instanceof Authorized && client.getToken() == null) {
                    printer.print("Access denied. Unauthorized");
                    continue;
                } else if (clientCommand.isPresent()) {
                    clientCommand.get().execute(args);
                    continue;
                }
                var serverCommand = commandManager.getServerCommand(commandName);
                if (serverCommand.isEmpty()) {
                    printer.print("Invalid command");
                    continue;
                }
                if (!ArgumentValidator.validate(serverCommand.get().args(), args)) {
                    printer.print("Invalid arguments");
                    continue;
                }
                var validateRequest = new ValidationRequest(serverCommand.get().name(), args);
                var validateResponse = (ValidationResponse) client.sendAndReceive(validateRequest);

                if (validateResponse.status() != StatusCode.OK) {
                    printer.print(validateResponse.message());
                    continue;
                }
                var commandRequest = new CommandRequest(serverCommand.get().name(), args);
                if (serverCommand.get().modelRequired()) {
                    commandRequest.setModel(new FlatParser(scanner, printer).parseFlat());
                }
                commandRequest.setToken(client.getToken());
                var commandResponse = (CommandResponse) client.sendAndReceive(commandRequest);
                client.setToken(commandResponse.token());
                printer.print(commandResponse.message());

            } catch (InterruptCommandException e) {
                printer.print("\nExited\n");
            } catch (NoSuchElementException e) {
                printer.print("EOF");
                break;
            } catch (InvalidArgsException e) {
                printer.print("Invalid args");
            } catch (RecursionException e) {
                printer.print("Recursion depth exceeded!");
                break;
            } catch (ExitException e) {
                provider.closeScanner();
                provider.getPrinter().print("Program has finished. Good luck!");
                break;
            } catch (TimeLimitExceededException e) {
                provider.getPrinter().print(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
//                provider.getPrinter().print("Error occurred while I/O");
            } catch (ClassNotFoundException e) {
                provider.getPrinter().print("Invalid response format from server");
            }
        }
    }
}


