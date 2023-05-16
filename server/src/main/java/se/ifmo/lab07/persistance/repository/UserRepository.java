package se.ifmo.lab07.persistance.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.ifmo.lab07.exception.NotFoundException;
import se.ifmo.lab07.persistance.entity.User;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository implements Repository<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String USER = "postgres";

    private static final String PASSWORD = "password";

    private static final String UPDATE_SQL = """
            UPDATE "user" SET username = ?, password = ? WHERE id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO "user"(username, password, salt) values (?, ?, ?);
            """;

    private static final String FIND_BY_USERNAME = """
            SELECT * FROM "user" WHERE username = ?;
            """;

    private static final String FIND_BY_ID = """
            SELECT * FROM "user" WHERE id = ?;
            """;

    private static final String DELETE_BY_ID = """
            DELETE FROM "user" WHERE id = ?;
            """;

    @Override
    public User save(User user) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (user.id() == null) {
                var statement = connection.prepareStatement(SAVE_SQL);
                statement.setString(1, user.username());
                statement.setString(2, user.password());
                statement.setString(3, user.salt());
                logger.error(statement.toString());
                statement.executeUpdate();

                return findByUsername(user.username()).orElseThrow(() -> new NotFoundException("User not found"));
            }
            var statement = connection.prepareStatement(UPDATE_SQL);
            statement.setString(1, user.username());
            statement.setString(2, user.password());
            statement.setInt(3, user.id());
            statement.executeUpdate();
            return user;
        }
    }

    @Override
    public Optional<User> getById(long id) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(mapRowToEntity(resultSet));
        }
    }

    @Override
    public boolean deleteById(long id) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.prepareStatement(DELETE_BY_ID);
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.prepareStatement(FIND_BY_USERNAME);
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(mapRowToEntity(resultSet));
        }
    }

    private User mapRowToEntity(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("salt")
        );
    }
}
