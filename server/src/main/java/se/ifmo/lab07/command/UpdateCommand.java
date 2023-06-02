package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

import java.sql.SQLException;

public class UpdateCommand extends Command {

    private static final Class<?>[] ARGS = new Class<?>[]{Long.class};

    public UpdateCommand(IOProvider provider, CollectionManager collection) {
        super("update {id} {element}", "обновить значение элемента коллекции, id которого равен заданному",
                provider, collection);
        this.requiresModel = true;
    }

    @Override
    public void validateArgs(Request request) throws InvalidArgsException {
        super.validateArgs(request);
        long id = Long.parseLong(request.args()[0]);

        var flat = collection.get(id);
        if (flat == null) {
            throw new InvalidArgsException("Flat with specified ID doesn't exist");
        }

        var user = getUserByRequest(request);
        if (!user.username().equals(flat.owner().username())) {
            throw new InvalidArgsException("You can't modify flats you don't own");
        }
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        long flatId = Long.parseLong(request.args()[0]);
        var flat = flatRepository.findById(flatId).orElseThrow(() -> new InvalidArgsException("Flat with specified ID doesn't exist"));

        flat.update(request.model());
        flat = flatRepository.save(flat);

        collection.update(flatId, flat);

        return new CommandResponse("Flat (ID %s) updated successfully.".formatted(flatId), StatusCode.OK, request.credentials());
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
