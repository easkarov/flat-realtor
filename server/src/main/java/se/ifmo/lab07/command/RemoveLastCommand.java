package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class RemoveLastCommand extends Command {
    public RemoveLastCommand(IOProvider provider, CollectionManager collection) {
        super("remove_last", "удалить последний элемент из коллекции", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.pop();
        return new CommandResponse("Last collection element removed successfully.", StatusCode.OK, request.token());
    }
}
