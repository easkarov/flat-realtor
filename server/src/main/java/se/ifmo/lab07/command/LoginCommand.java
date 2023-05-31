package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.manager.AuthManager;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;

public class LoginCommand extends Command implements Unauthorized {

    private static final Class<?>[] ARGS = new Class<?>[]{String.class, String.class};

    private final AuthManager authManager;

    public LoginCommand(IOProvider provider, CollectionManager collection, AuthManager authManager) {
        super("login {username} {password}", "войти", provider, collection);
        this.authManager = authManager;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());

        try {
            var token = authManager.authorize(request.args()[0], request.args()[1]);
            return new CommandResponse("Logged in successfully", StatusCode.OK, token);
        } catch (AuthorizationException e) {
            return new CommandResponse(e.getMessage(), StatusCode.ERROR, request.token());
        }
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
