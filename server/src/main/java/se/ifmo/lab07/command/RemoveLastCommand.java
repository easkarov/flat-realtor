package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

import java.sql.SQLException;

public class RemoveLastCommand extends Command {
    public RemoveLastCommand(IOProvider provider, CollectionManager collection) {
        super("remove_last", "удалить последний элемент из коллекции", provider, collection);
    }

    @Override
    public void validateArgs(Request request) throws InvalidArgsException {
        super.validateArgs(request);
        var flat = collection.last();
        if (flat == null) {
            throw new InvalidArgsException("Collection is empty");
        }
        var user = getUserByRequest(request);
        if (!user.username().equals(flat.owner().username())) {
            throw new InvalidArgsException("You can't remove flats you don't own");
        }
    }


    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        var user = getUserByRequest(request);
        var flat = collection.last();

        if (!flat.owner().username().equals(user.username())) {
            return new CommandResponse("You can't remove flats you don't own", StatusCode.ERROR, request.credentials());
        }

        flatRepository.deleteById(flat.id());
        collection.removeById(flat.id());

        return new CommandResponse("Last flat removed successfully.", StatusCode.OK, request.credentials());
    }
}
