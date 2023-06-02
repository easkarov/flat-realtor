package se.ifmo.lab07.command;

import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.request.Request;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.entity.User;
import se.ifmo.lab07.exception.AuthorizationException;
import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.persistance.repository.FlatRepository;
import se.ifmo.lab07.persistance.repository.HouseRepository;
import se.ifmo.lab07.persistance.repository.UserRepository;
import se.ifmo.lab07.util.ArgumentValidator;
import se.ifmo.lab07.util.IOProvider;

import java.sql.SQLException;

public abstract class Command {

    private static final Class<?>[] ARGS = new Class<?>[]{};

    private final String name;
    private final String description;
    IOProvider provider;
    CollectionManager collection;
    boolean requiresModel;
    UserRepository userRepository;
    FlatRepository flatRepository;

    public Command(String name, String description, IOProvider provider, CollectionManager collection) {
        this.name = name;
        this.description = description;
        this.provider = provider;
        this.collection = collection;
        this.requiresModel = false;
        this.userRepository = new UserRepository();
        this.flatRepository = new FlatRepository(new HouseRepository(), userRepository);
    }

    public abstract Response execute(CommandRequest args) throws SQLException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return String.format("%40s    |     %s", name, description);
    }

    public boolean isRequiresModel() {
        return requiresModel;
    }

    public void validateArgs(Request request) {
        if (!ArgumentValidator.validate(getArgumentTypes(), request.args())) {
            throw new InvalidArgsException();
        }
    }

    public Class<?>[] getArgumentTypes() {
        return ARGS;
    }

    User getUserByRequest(Request request) {
        return userRepository.findByUsername(request.credentials().username()).orElseThrow(() -> new AuthorizationException("Access denied"));
    }
}
