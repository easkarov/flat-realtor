package se.ifmo.lab07.command;


import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class InfoCommand extends Command {
    public InfoCommand(IOProvider provider, CollectionManager collection) {
        super("info", "вывести информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)",
                provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        return new CommandResponse(collection.description());
    }
}
