package se.ifmo.lab07.command;

import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class ClearCommand extends Command {
    public ClearCommand(IOProvider provider, CollectionManager collection) {
        super("clear", "очистить коллекцию", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.clear();
        return new CommandResponse("Collection cleared successfully.\n");
    }
}
