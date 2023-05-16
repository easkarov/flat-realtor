package se.ifmo.lab07.manager;

import se.ifmo.lab07.network.Client;
import se.ifmo.lab07.command.Command;
import se.ifmo.lab07.command.ExecuteScriptCommand;
import se.ifmo.lab07.command.ExitCommand;
import se.ifmo.lab07.command.HelpCommand;
import se.ifmo.lab07.dto.CommandDTO;
import se.ifmo.lab07.util.IOProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, CommandDTO> serverCommands = new HashMap<>();
    private final Map<String, Command> clientCommands = new HashMap<>();

    public CommandManager(Client client, IOProvider provider, int recDepth) {
        clientCommands.put("help", new HelpCommand(provider, client, clientCommands, serverCommands));
        clientCommands.put("execute_script", new ExecuteScriptCommand(provider, client, recDepth));
        clientCommands.put("exit", new ExitCommand(provider, client));
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
}
