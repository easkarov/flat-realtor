package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.AuthManager;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

public class LogoutCommand extends Command implements Unauthorized {

    private final AuthManager authManager;

    public LogoutCommand(IOProvider provider, CollectionManager collection, AuthManager authManager) {
        super("logout", "выйти", provider, collection);
        this.authManager = authManager;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        authManager.logout(request.token());

        return new CommandResponse("Logged out successfully");
    }
}
