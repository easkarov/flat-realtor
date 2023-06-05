package se.ifmo.lab07.persistance;

import se.ifmo.lab07.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    private static final String CREATE_SQL = Configuration.DB_INIT;

    private static final String DROP_SQL = Configuration.DB_DROP;

    private static final String URL = Configuration.DB_URL;

    private static final String USER = Configuration.DB_USER;

    private static final String PASSWORD = Configuration.DB_PASSWORD;

    private static void executeFromFile(String fileName) throws SQLException, IOException {
        var connection = DriverManager.getConnection(URL, USER, PASSWORD);
        var statement = connection.createStatement();
        var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            throw new FileNotFoundException("Specified file (%s) not found".formatted(fileName));
        }
        try (var sc = new Scanner(stream)) {
            var builder = new StringBuilder();
            while (sc.hasNext()) {
                builder.append(sc.nextLine());
            }
            statement.execute(builder.toString());
        }
    }

    public static void init() throws SQLException, IOException {
        executeFromFile(CREATE_SQL);
    }

    public static void drop() throws SQLException, IOException {
        executeFromFile(DROP_SQL);
    }
}
