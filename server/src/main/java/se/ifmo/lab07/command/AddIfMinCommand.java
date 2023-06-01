package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

import java.sql.SQLException;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(IOProvider provider, CollectionManager collection) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
                provider, collection);
        this.requiresModel = true;
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        var flat = request.model();
        var user = getUserByRequest(request);
        flat.owner(user);

        var minFlat = collection.min();
        if (minFlat.area() <= flat.area()) {
            var message = "Flat (value: %s) not added because there is flat with less value (%s).\n".formatted(
                    flat.area(), minFlat.area()
            );
            return new CommandResponse(message, StatusCode.OK, request.token());
        }
        flat = flatRepository.save(request.model());
        collection.push(flat);
        var message = "Flat (ID %s) added successfully.\n".formatted(flat.id());
        return new CommandResponse(message, StatusCode.OK, request.token());
    }
}
