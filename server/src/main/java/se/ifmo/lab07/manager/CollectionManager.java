package se.ifmo.lab07.manager;

import se.ifmo.lab07.entity.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class CollectionManager {
    private final Stack<Flat> collection;
    private final ZonedDateTime createdAt = ZonedDateTime.now();
    private final String fileName;

    private CollectionManager(String fileName) {
        this.collection = new Stack<>();
        this.fileName = fileName;
    }

    public List<Flat> getCollection() {
        return collection;
    }


    public static CollectionManager fromFile(String fileName) throws FileNotFoundException {
        CollectionManager collection = new CollectionManager(fileName);
        Flat[] flats = JsonManager.fromFile(new FileReader(fileName));
        for (Flat flat : flats) collection.push(flat);
        return collection;
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

    public void clear() {
        collection.clear();
    }

    public void pop() {
        if (!collection.isEmpty()) collection.pop();
    }

    public Flat get(long id) {
        for (Flat flat : collection) if (flat.id() == id) return flat;
        return null;
    }

    public String description() {
        return String.format("Тип: %s\nДата инициализации: %s\nКол-во элементов: %s",
                collection.getClass().getName(), createdAt, collection.size());
    }

    public void dump() throws IOException {
        JsonManager.toFile(fileName, collection);
    }

    public void update() throws IOException {
        try (var file = new FileReader(fileName)) {
            this.collection.clear();
            this.collection.addAll(List.of(JsonManager.fromFile(file)));
        }
    }

    public Flat min() {
        return !collection.isEmpty() ? collection.stream()
                .min(Flat::compareTo)
                .get() : null;
    }

    public void shuffle() {
        Collections.shuffle(collection);
    }

    public long removeByFurnish(Furnish furnish) {
        long n = collection.stream()
                .filter(flat -> flat.furnish() == furnish)
                .count();
        collection.removeIf(flat -> flat.furnish() == furnish);
        return n;
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
