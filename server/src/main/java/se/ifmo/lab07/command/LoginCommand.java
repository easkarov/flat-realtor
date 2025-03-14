package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.Credentials;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.AuthManager;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

public class LoginCommand extends Command implements Unauthorized {

    private static final Class<?>[] ARGS = new Class<?>[]{String.class, String.class};

    private final AuthManager authManager;

    public LoginCommand(IOProvider provider, CollectionManager collection, AuthManager authManager) {
        super("login {username} {password}", "войти", provider, collection);
        this.authManager = authManager;
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request);

        var username = request.args()[0];
        var password = request.args()[1];
        var user = authManager.authorize(username, password);
        var credentials = new Credentials(username, password, user.role());
        return new CommandResponse("Logged in successfully", StatusCode.OK, credentials);
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
