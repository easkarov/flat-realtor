package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

import java.sql.SQLException;

public class ClearCommand extends Command {
    public ClearCommand(IOProvider provider, CollectionManager collection) {
        super("clear", "очистить коллекцию", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);
        var user = getUserByRequest(request);
        flatRepository.deleteByOwnerId(user.id());
        long n = collection.removeByOwnerId(user.id());
        return new CommandResponse("All your %s flats cleared successfully.\n".formatted(n), StatusCode.OK, request.credentials());
    }
}
