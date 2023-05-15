package se.ifmo.lab07.command;

import se.ifmo.lab07.exception.InvalidArgsException;
import se.ifmo.lab07.manager.CollectionManager;
import se.ifmo.lab07.dto.request.CommandRequest;
import se.ifmo.lab07.dto.response.CommandResponse;
import se.ifmo.lab07.dto.response.Response;
import se.ifmo.lab07.model.Flat;
import se.ifmo.lab07.model.House;
import se.ifmo.lab07.util.IOProvider;

import java.util.*;
import java.util.stream.Collectors;

public class PrintUniqueHouseCommand extends Command {
    public PrintUniqueHouseCommand(IOProvider provider, CollectionManager collection) {
        super("print_unique_house",
                "вывести уникальные значения поля house всех элементов в коллекции",
                provider, collection);
    }

    @Override
    public Response execute(CommandRequest request) throws InvalidArgsException {
        validateArgs(request.args());
        Set<House> houseSet = collection.getCollection()
                .stream()
                .map(Flat::getHouse)
                .collect(Collectors.toSet());

        String line = "-".repeat(60);
        var builder = new StringBuilder();
        builder.append(line).append("\n");
        for (House house : houseSet) {
            builder.append(house.toString()).append("\n");
            builder.append(line).append("\n");
        }
        return new CommandResponse(builder.toString());
    }
}
