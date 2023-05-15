package se.ifmo.lab07.command;

import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class ShuffleCommand extends Command {
    public ShuffleCommand(IOProvider IOProvider, CollectionManager collection) {
        super("shuffle", "перемешать элементы коллекции в случайном порядке", IOProvider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        collection.shuffle();
        return new CommandResponse("Collection has been shuffled.");
    }
}
