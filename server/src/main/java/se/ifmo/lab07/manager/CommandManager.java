package se.ifmo.lab07.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.command.*;
import se.ifmo.lab07.dto.CommandDTO;
import se.ifmo.lab07.dto.StatusCode;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.ValidationRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.dto.response.ValidationResponse;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.util.IOProvider;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);

    private final AuthManager authManager;

    public CommandManager(CollectionManager collection, IOProvider provider, AuthManager authManager) {
        register("info", new InfoCommand(provider, collection));
        register("show", new ShowCommand(provider, collection));
        register("add", new AddCommand(provider, collection));
        register("update", new UpdateCommand(provider, collection));
        register("remove_by_id", new RemoveByIdCommand(provider, collection));
        register("clear", new ClearCommand(provider, collection));
        register("remove_last", new RemoveLastCommand(provider, collection));
        register("add_if_min", new AddIfMinCommand(provider, collection));
        register("shuffle", new ShuffleCommand(provider, collection));
        register("remove_all_by_furnish", new RemoveByFurnishCommand(provider, collection));
        register("filter_starts_with_name", new FilterNameCommand(provider, collection));
        register("print_unique_house", new PrintUniqueHouseCommand(provider, collection));
        register("sign_up", new SignUpCommand(provider, collection, authManager));
        register("login", new LoginCommand(provider, collection, authManager));
        register("logout", new LogoutCommand(provider, collection));
        this.authManager = authManager;
    }

    public List<CommandDTO> getCommandsDTO() {
        return commands.entrySet()
                .stream()
                .map(c -> new CommandDTO(
                        c.getKey(),
                        c.getValue().getDescription(),
                        c.getValue().getArgumentTypes(),
                        c.getValue().isRequiresModel())
                )
                .toList();
    }

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Response execute(CommandRequest request) {
        try {
            if (!commands.containsKey(request.name())) {
                return new CommandResponse("Invalid command", StatusCode.ERROR, request.credentials());
            }
            if (!(commands.get(request.name()) instanceof Unauthorized)) {
                authManager.authorize(request.credentials());
            }
            var command = commands.get(request.name());
            return command.execute(request);
        } catch (InvalidArgsException e) {
            logger.error(e.toString());
            return new CommandResponse(e.getMessage(), StatusCode.ERROR, request.credentials());
        } catch (AuthorizationException e) {
            logger.error(e.toString());
            return new CommandResponse(e.getMessage(), StatusCode.ERROR);
        } catch (SQLException e) {
            logger.error(e.toString());
            return new CommandResponse("Something went wrong with DB", StatusCode.ERROR);
        }
    }

    public Response validate(ValidationRequest request) {
        try {
            logger.info("authorization... {}", request);
            if (!commands.containsKey(request.name())) {
                return new ValidationResponse("Invalid command", StatusCode.ERROR, request.credentials());
            }
            if (!(commands.get(request.name()) instanceof Unauthorized)) {
                authManager.authorize(request.credentials());
            }
            var command = commands.get(request.name());
            command.validateArgs(request);
            return new ValidationResponse("OK", StatusCode.OK, request.credentials());
        } catch (InvalidArgsException e) {
            logger.error(e.toString());
            return new ValidationResponse(e.getMessage(), StatusCode.ERROR, request.credentials());
        } catch (AuthorizationException e) {
            logger.error(e.toString());
            return new ValidationResponse(e.getMessage(), StatusCode.ERROR);
        }
    }


}
