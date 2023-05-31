package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.entity.Flat;
import se.ifmo.lab07.util.IOProvider;

import java.util.List;

public class FilterNameCommand extends Command {

    private static final Class<?>[] ARGS = new Class<?>[]{String.class};

    public FilterNameCommand(IOProvider provider, CollectionManager collection) {
        super("filter_starts_with_name {name}",
                "вывести элементы, значение поля name которых начинается с заданной подстроки",
                provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        String name = request.args()[0];
        List<Flat> flats = collection.getCollection()
                .stream()
                .filter(flat -> flat.name().toLowerCase().startsWith(name.toLowerCase()))
                .toList();

        var builder = new StringBuilder();
        builder.append("Collection filtered by name:\n");
        var line = "-".repeat(60) + "\n";
        builder.append(line);
        for (Flat flat : flats) {
            builder.append(flat.toString()).append("\n");
            builder.append(line);
        }
        return new CommandResponse(builder.toString(), StatusCode.OK, request.token());
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
