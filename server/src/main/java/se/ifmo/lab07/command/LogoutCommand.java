package se.ifmo.lab07.command;

import se.ifmo.lab07.manager.AuthManager;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class LogoutCommand extends Command {

    private static final Class<?>[] ARGS = new Class<?>[]{String.class, String.class};

    private final AuthManager authManager;

    public LogoutCommand(IOProvider provider, CollectionManager collection, AuthManager authManager) {
        super("logout {username} {password}", "зарегистрироваться", provider, collection);
        this.authManager = authManager;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        authManager.logout(request.args()[0]);

        return new CommandResponse("Logged in successfully");
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
