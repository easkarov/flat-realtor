package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

import java.sql.SQLException;

public class AddCommand extends Command {
    public AddCommand(IOProvider provider, CollectionManager collection) {
        super("add", "добавить новый элемент в коллекцию", provider, collection);
        this.requiresModel = true;
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        var flat = request.model();
        var user = getUserByRequest(request);
        flat.owner(user);

        flat = flatRepository.save(flat);
        collection.push(flat);

        var message = "Flat (ID %s) added successfully.\n".formatted(flat.id());
        return new CommandResponse(message, StatusCode.OK, request.token());
    }
}
