package se.ifmo.lab07.command;

import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class AddIfMinCommand extends Command {
    public AddIfMinCommand(IOProvider provider, CollectionManager collection) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
                provider, collection);
        this.requiresModel = true;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        var flat = request.model();
        var minFlat = collection.min();
        if (minFlat.getArea() <= flat.getArea()) {
            var message = "Flat (value: %s) not added because there is flat with less value (%s).\n".formatted(
                    flat.getArea(), minFlat.getArea()
            );
            return new CommandResponse(message);
        }
        collection.push(flat);
        var message = "Flat (ID %s) added successfully.\n".formatted(flat.getId());
        return new CommandResponse(message);
    }
}
