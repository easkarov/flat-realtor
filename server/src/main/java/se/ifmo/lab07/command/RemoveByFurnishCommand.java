package se.ifmo.lab07.command;

import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.model.Furnish;
import se.ifmo.lab07.util.IOProvider;

public class RemoveByFurnishCommand extends Command {
    
    private static final Class<?>[] ARGS = new Class<?>[]{Furnish.class};
    public RemoveByFurnishCommand(IOProvider provider, CollectionManager collection) {
        super("remove_all_by_furnish {furnish}",
                "удалить из коллекции все элементы, значение поля furnish которого эквивалентно заданному",
                provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        Furnish furnish = Furnish.valueOf(request.args()[0]);
        long n = collection.removeByFurnish(furnish);
        return new CommandResponse("%s flats with Furnish [%s] removed successfully.\n".formatted(n, furnish));
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
