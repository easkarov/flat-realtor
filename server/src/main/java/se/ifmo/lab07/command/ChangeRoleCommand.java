package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.Role;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.util.IOProvider;

import java.sql.SQLException;

public class ChangeRoleCommand extends Command {
    private static final Class<?>[] ARGS = new Class<?>[]{Integer.class, Role.class};

    public ChangeRoleCommand(IOProvider provider, CollectionManager collection) {
        super("change_role {user_id} {role}", "поменять роль польвателя по id",
                provider, collection);
    }

    @Override
    public void validateArgs(Request request) throws InvalidArgsException {
        super.validateArgs(request);
        int id = Integer.parseInt(request.args()[0]);
        userRepository.findById(id).orElseThrow(() -> new InvalidArgsException("User not found"));
    }

    @Override
    public Response execute(CommandRequest request) throws SQLException {
        validateArgs(request);

        int id = Integer.parseInt(request.args()[0]);
        Role role = Role.valueOf(request.args()[1]);

        var user = userRepository.findById(id).orElseThrow(() -> new InvalidArgsException("User not found"));
        user.role(role);
        userRepository.save(user);

        return new CommandResponse("User role has been changed", StatusCode.OK, request.credentials());
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }
}
