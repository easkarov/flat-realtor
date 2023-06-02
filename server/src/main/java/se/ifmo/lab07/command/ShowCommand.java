package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.entity.Flat;
import se.ifmo.lab07.util.IOProvider;

public class ShowCommand extends Command {
    public ShowCommand(IOProvider provider, CollectionManager collection) {
        super("show", "вывести все элементы коллекции в строковом представлении", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request);
        var builder = new StringBuilder();
        String line = "-".repeat(60);
        builder.append(line).append("\n");
        for (Flat flat : collection.getCollection()) {
            builder.append(flat.toString()).append("\n");
            builder.append(line).append("\n");
        }
        return new CommandResponse(builder.toString(), StatusCode.OK, request.credentials());
    }
}
