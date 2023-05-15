package se.ifmo.lab07.util;

import java.util.Arrays;

public class ArgumentValidator {
    public static boolean validate(Class<?>[] types, String[] args) {
        if (types.length != args.length) {
            return false;
        }

        for (int i = 0; i < args.length; i++) {
            var type = types[i];
            var arg = args[i];

            if (type.isEnum()) {
                var enumStrings = Arrays.stream(type.getEnumConstants()).map(Object::toString).toList();
                if (!enumStrings.contains(arg.toUpperCase())) {
                    return false;
                }
            } else if (type == Integer.class || type == Long.class) {
                try {
                    Long.parseLong(arg);
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (type == Double.class || type == Float.class) {
                try {
                    Double.parseDouble(arg);
                } catch (NumberFormatException e) {
                    return false;
                }
            } else if (type != String.class) {
                return false;
            }
        }
        return true;
    }
}
