package se.ifmo.lab07.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import se.ifmo.lab07.model.Flat;
import se.ifmo.lab07.util.ZonedDateTimeDeserializer;
import se.ifmo.lab07.util.ZonedDateTimeSerializer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Scanner;

public class JsonManager {
    public static Flat[] fromFile(FileReader file) throws JsonParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        Gson gson = builder.create();
        StringBuilder inputData = new StringBuilder();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            inputData.append(scanner.nextLine());
        }
        return gson.fromJson(inputData.toString(), Flat[].class);
    }

    public static void toFile(String filename, Object object) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer());
        Gson gson = builder.create();
        String json = gson.toJson(object);
        try (var file = new FileWriter(filename)) {
            file.write(json);
            file.flush();
        }
    }
}
