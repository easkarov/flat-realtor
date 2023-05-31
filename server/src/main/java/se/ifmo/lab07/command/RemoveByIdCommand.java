package se.ifmo.lab07.command;

import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class RemoveByIdCommand extends Command {
    
    private static final Class<?>[] ARGS = new Class<?>[]{Long.class};
    
    public RemoveByIdCommand(IOProvider provider, CollectionManager collection) {
        super("remove_by_id {id}", "удалить элемент из коллекции по его id", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        long flatId = Long.parseLong(request.args()[0]);
        if (collection.get(flatId) == null) {
            return new CommandResponse("Flat with specified ID doesn't exist.", StatusCode.ERROR, request.token());
        }
        collection.removeById(flatId);
        return new CommandResponse("Flat (ID %s) removed successfully.\n".formatted(flatId), StatusCode.OK, request.token());
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
