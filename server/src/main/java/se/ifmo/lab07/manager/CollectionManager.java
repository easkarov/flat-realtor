package se.ifmo.lab07.manager;

import se.ifmo.lab07.entity.Flat;
import se.ifmo.lab07.entity.Furnish;
import se.ifmo.lab07.entity.House;
import se.ifmo.lab07.persistance.repository.FlatRepository;
import se.ifmo.lab07.persistance.repository.HouseRepository;
import se.ifmo.lab07.persistance.repository.UserRepository;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class CollectionManager {
    private final Stack<Flat> collection;
    private final ZonedDateTime createdAt = ZonedDateTime.now();

    private CollectionManager() {
        this.collection = new Stack<>();
    }

    public List<Flat> getCollection() {
        return collection;
    }

    public static CollectionManager fromDatabase() {
        var collection = new CollectionManager();
        var repository = new FlatRepository(new HouseRepository(), new UserRepository());
        try {
            for (Flat flat : repository.findAll()) {
                collection.push(flat);
            }
            return collection;
        } catch (SQLException e) {
            return new CollectionManager();
        }
    }


    public void push(Flat element) {
        if (element.validate() && get(element.id()) == null) {
            collection.push(element);
        }
    }

    public void update(long id, Flat newFlat) {
        Flat flat = get(id);
        flat.update(newFlat);
    }

    public void removeById(long id) {
        collection.removeElement(get(id));
    }

    public long removeByOwnerId(int id) {
        long n = collection.stream()
                .filter(flat -> flat.owner().id().equals(id))
                .count();
        collection.removeIf(flat -> flat.owner().id().equals(id));
        return n;
    }

    public Flat last() {
        return collection.isEmpty() ? null : collection.peek();
    }

    public Flat get(long id) {
        for (Flat flat : collection) if (flat.id() == id) return flat;
        return null;
    }

    public String description() {
        return String.format("Тип: %s\nДата инициализации: %s\nКол-во элементов: %s",
                collection.getClass().getName(), createdAt, collection.size());
    }

    public Flat min() {
        return !collection.isEmpty() ? collection.stream()
                .min(Flat::compareTo)
                .get() : null;
    }

    public void shuffle() {
        Collections.shuffle(collection);
    }

    public long removeByFurnish(String username, Furnish furnish) {
        long n = collection.stream()
                .filter(flat -> flat.furnish() == furnish && flat.owner().username().equals(username))
                .count();
        collection.removeIf(flat -> flat.furnish() == furnish && flat.owner().username().equals(username));
        return n;
    }

    public List<Flat> filterByName(String name) {
        return collection.stream()
                .filter(flat -> flat.name().toLowerCase().startsWith(name.toLowerCase()))
                .toList();
    }

    public Set<House> getUniqueHouses() {
        return collection.stream()
                .map(Flat::house)
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Flat flat : collection) {
            builder.append(flat);
            builder.append("\n");
        }
        return builder.toString();
    }
}
