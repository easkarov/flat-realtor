package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.Role;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.exception.ExitException;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.util.IOProvider;

public class ExitCommand extends Command {
    public ExitCommand(IOProvider provider, Client client) {
        super("exit", "завершить программу (без сохранения в файл)", provider, client, Role.MIN_USER);
    }

    @Override
    public void execute(String[] args) throws InvalidArgsException {
        validateArgs(args);
        throw new ExitException();
    }
}
