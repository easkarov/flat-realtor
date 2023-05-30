package se.ifmo.lab07.persistance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String CREATE_SQL = "sql/create.sql";

    private static final String DROP_SQL = "sql/drop.sql";

    private static final String USER = "postgres";

    private static final String PASSWORD = "password";

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
