package se.ifmo.lab07.manager;

import se.ifmo.lab07.command.*;
import se.ifmo.lab07.dto.CommandDTO;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.ValidationRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.ValidationResponse;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.exception.RoleException;
import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.parser.FlatParser;
import se.ifmo.lab07.util.ArgumentValidator;
import se.ifmo.lab07.util.IOProvider;

import javax.naming.TimeLimitExceededException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, CommandDTO> serverCommands = new HashMap<>();
    private final Map<String, Command> clientCommands = new HashMap<>();
    private final Client client;
    private final IOProvider provider;

    public CommandManager(Client client, IOProvider provider, int recDepth) {
        clientCommands.put("help", new HelpCommand(provider, client, clientCommands, serverCommands));
        clientCommands.put("execute_script", new ExecuteScriptCommand(provider, client, recDepth));
        clientCommands.put("exit", new ExitCommand(provider, client));
        this.client = client;
        this.provider = provider;
    }

    public void register(List<CommandDTO> commandsToAdd) {
        serverCommands.putAll(commandsToAdd.stream().collect(Collectors.toMap(CommandDTO::name, Function.identity())));
    }

    public Optional<CommandDTO> getServerCommand(String commandName) {
        return Optional.ofNullable(serverCommands.get(commandName));
    }

    public Optional<Command> getClientCommand(String commandName) {
        return Optional.ofNullable(clientCommands.get(commandName));
    }

    public Map<String, CommandDTO> getServerCommands() {
        return serverCommands;
    }

    public void executeClientCommand(String commandName, String[] args) {
        var clientCommand = getClientCommand(commandName);
        if (clientCommand.isEmpty()) {
            return;
        }
        if (clientCommand.get() instanceof Authorized) {
            if (client.credentials() == null) {
                throw new AuthorizationException("Access denied. Unauthorized");
            } else if (client.credentials().role().weight() < clientCommand.get().getRole().weight()) {
                throw new RoleException("Permission denied");
            }
        }
        clientCommand.get().execute(args);
    }

    public void executeServerCommand(String commandName, String[] args) throws IOException, TimeLimitExceededException, ClassNotFoundException {
        var serverCommand = getServerCommand(commandName);

        if (serverCommand.isEmpty()) {
            return;
        }
        if (!ArgumentValidator.validate(serverCommand.get().args(), args)) {
            throw new InvalidArgsException();
        }
        var validateRequest = new ValidationRequest(serverCommand.get().name(), args);
        var validateResponse = (ValidationResponse) client.sendAndReceive(validateRequest);

        if (validateResponse.status() != StatusCode.OK) {
            throw new InvalidArgsException(validateResponse.message());
        }

        var commandRequest = new CommandRequest(serverCommand.get().name(), args);
        if (serverCommand.get().modelRequired()) {
            commandRequest.setModel(new FlatParser(provider.getScanner(), provider.getPrinter()).parseFlat());
        }
        commandRequest.setCredentials(client.credentials());
        var commandResponse = (CommandResponse) client.sendAndReceive(commandRequest);
        client.setCredentials(commandResponse.credentials());
        provider.getPrinter().print(commandResponse.message());
    }
}
