package se.ifmo.lab07.persistance.repository;

import se.ifmo.lab07.exception.NotFoundException;
import se.ifmo.lab07.persistance.entity.*;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Optional;

public class FlatRepository implements Repository<Flat> {

    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String USER = "postgres";


    private static final String PASSWORD = "password";

    private static final String UPDATE_SQL = """
            UPDATE flat SET
            name = ?,
            x_coord = ?,
            y_coord = ?,
            area = ?,
            number_of_rooms = ?,
            furnish = ?,
            view = ?,
            transport = ?,
            house_id = ?
            WHERE id = ?;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO flat(
                name,
                x_coord,
                y_coord,
                area,
                number_of_rooms,
                furnish,
                view,
                transport,
                house_id,
                owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String FIND_BY_ID = """
            SELECT * FROM flat WHERE id = ?;
            """;

    private static final String DELETE_BY_ID = """
            DELETE FROM flat WHERE id = ?;
            """;

    private final HouseRepository houseRepository;

    private final UserRepository userRepository;

    public FlatRepository(HouseRepository houseRepository, UserRepository userRepository) {
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flat save(Flat flat) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);
            if (getById(flat.id()).isEmpty()) {
                var statement = connection.prepareStatement(SAVE_SQL);
                statement.setString(1, flat.name());
                statement.setLong(2, flat.coordinates().x());
                statement.setFloat(3, flat.coordinates().y());
                statement.setFloat(4, flat.area());
                statement.setLong(5, flat.numberOfRooms());
                statement.setString(6, flat.furnish() == null ? null : flat.furnish().name());
                statement.setString(7, flat.view().name());
                statement.setString(8, flat.transport() == null ? null : flat.transport().name());
                statement.setInt(9, flat.house().id());
                statement.setInt(10, flat.owner().id());
                statement.executeUpdate();
                // saving nested objects
                userRepository.save(flat.owner());
                houseRepository.save(flat.house());

                connection.commit();

                return getById(flat.id()).orElseThrow(() -> new NotFoundException("Flat not found"));
            }

            var statement = connection.prepareStatement(UPDATE_SQL);
            statement.setString(1, flat.name());
            statement.setLong(2, flat.coordinates().x());
            statement.setFloat(3, flat.coordinates().y());
            statement.setFloat(4, flat.area());
            statement.setLong(5, flat.numberOfRooms());
            statement.setString(6, flat.furnish() == null ? null : flat.furnish().name());
            statement.setString(7, flat.view().name());
            statement.setString(8, flat.transport() == null ? null : flat.transport().name());
            statement.setInt(9, flat.house().id());
            statement.setLong(10, flat.id());
            statement.executeUpdate();
            // saving nested objects
            userRepository.save(flat.owner());
            houseRepository.save(flat.house());

            connection.commit();

            return flat;
        }
    }

    @Override
    public Optional<Flat> getById(long id) throws SQLException {
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

    private Flat mapRowToEntity(ResultSet resultSet) throws SQLException {
        var x = resultSet.getLong("x_coord");
        var y = resultSet.getFloat("y_coord");
        var coordinates = new Coordinates(x, y);

        var house = houseRepository.getById(resultSet.getLong("house_id")).orElseThrow(() -> new NotFoundException("House not found"));
        var owner = userRepository.getById(resultSet.getLong("owner_id")).orElseThrow(() -> new NotFoundException("User not found"));

        return new Flat(resultSet.getLong("id"),
                resultSet.getString("name"),
                coordinates,
                resultSet.getObject("created_at", ZonedDateTime.class),
                resultSet.getLong("area"),
                resultSet.getLong("number_of_rooms"),
                Optional.ofNullable(resultSet.getString("furnish")).map(Furnish::valueOf).orElse(null),
                View.valueOf(resultSet.getString("view")),
                Optional.ofNullable(resultSet.getString("transport")).map(Transport::valueOf).orElse(null),
                house,
                owner
        );
    }
}
