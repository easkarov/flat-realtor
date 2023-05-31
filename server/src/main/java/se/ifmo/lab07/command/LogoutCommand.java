package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

public class LogoutCommand extends Command implements Unauthorized {

    public LogoutCommand(IOProvider provider, CollectionManager collection) {
        super("logout", "выйти", provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        return new CommandResponse("Logged out successfully");
    }
}
