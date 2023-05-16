package se.ifmo.lab07.persistance;

import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String CREATE_SQL = "create.sql";

    private static final String DROP_SQL = "create.sql";

    private static void executeFromFile(String fileName) throws SQLException, IOException {
        var connection = DriverManager.getConnection(URL);
        var statement = connection.createStatement();

        try (var sc = new Scanner(new FileReader(fileName))) {
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
