package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

import java.sql.SQLException;

public class RemoveByIdCommand extends Command {

    private static final Class<?>[] ARGS = new Class<?>[]{Long.class};

    public RemoveByIdCommand(IOProvider provider, CollectionManager collection) {
        super("remove_by_id {id}", "удалить элемент из коллекции по его id", provider, collection);
    }

    @Override
    public void validateArgs(Request request) throws InvalidArgsException {
        super.validateArgs(request);
        long id = Long.parseLong(request.args()[0]);
        if (collection.get(id) == null) {
            throw new InvalidArgsException("Flat with specified ID doesn't exist");
        }
        var user = getUserByRequest(request);
        if (!collection.get(id).owner().username().equals(user.username())) {
            throw new InvalidArgsException("You can't remove flats you don't own");
        }
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        long flatId = Long.parseLong(request.args()[0]);

        flatRepository.deleteById(flatId);
        collection.removeById(flatId);

        return new CommandResponse("Flat (ID %s) removed successfully.\n".formatted(flatId), StatusCode.OK, request.credentials());
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
