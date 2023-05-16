package se.ifmo.lab07.command;

import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CommandManager;
import se.ifmo.lab07.parser.CommandParser;
import se.ifmo.lab07.util.IOProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ExecuteScriptCommand extends Command {

    private static final Class<?>[] ARGS = new Class<?>[]{String.class};

    private final int recDepth;
    public ExecuteScriptCommand(IOProvider provider, Client client, int recDepth) {
        super("execute_script {file_name}", "считать и исполнить скрипт из указанного файла", provider, client);
        this.recDepth = recDepth;
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args);
        String fileName = args[0];
        try (FileReader fileReader = new FileReader(fileName)) {
            var provider = new IOProvider(new Scanner(fileReader), this.provider.getPrinter());
            var commandManager = new CommandManager(client, provider, recDepth + 1);
            var commandParser =  new CommandParser(client, commandManager, provider, recDepth + 1);
            commandParser.run();
        } catch (FileNotFoundException e) {
            provider.getPrinter().print("File not found or access denied (read).");
        } catch (IOException e) {
            provider.getPrinter().print("Something went wrong while reading.");
        }
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}