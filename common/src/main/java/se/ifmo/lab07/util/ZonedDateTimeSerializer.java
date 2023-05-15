package se.ifmo.lab07.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

public class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime> {
    @Override
    public JsonElement serialize(ZonedDateTime datetime, Type type, JsonSerializationContext jsonSerializationContext) throws JsonParseException {
        return new JsonPrimitive(datetime.toString());
    }
}
