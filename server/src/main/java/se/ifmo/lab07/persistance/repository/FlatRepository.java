package se.ifmo.lab07.persistance.repository;

import se.ifmo.lab07.entity.*;
import se.ifmo.lab07.exception.NotFoundException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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
            house_id = ?,
            owner_id = ?
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
                owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING *;
            """;

    private static final String FIND_BY_ID = """
            SELECT * FROM flat WHERE id = ?;
            """;

    private static final String DELETE_BY_ID = """
            DELETE FROM flat WHERE id = ?;
            """;

    private static final String FIND_ALL = """
            SELECT * FROM flat;
            """;

    private static final String DELETE_BY_OWNER_ID = """
            DELETE FROM flat WHERE owner_id = ?;
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
            if (findById(flat.id()).isEmpty()) {
                var statement = connection.prepareStatement(SAVE_SQL);
                statement.setString(1, flat.name());
                statement.setLong(2, flat.coordinates().x());
                statement.setFloat(3, flat.coordinates().y());
                statement.setFloat(4, flat.area());
                statement.setLong(5, flat.numberOfRooms());
                statement.setString(6, flat.furnish() == null ? null : flat.furnish().name());
                statement.setString(7, flat.view().name());
                statement.setString(8, flat.transport() == null ? null : flat.transport().name());
                // saving nested objects
                var owner = userRepository.save(flat.owner());
                var house = houseRepository.save(flat.house());

                statement.setInt(9, house.id());
                statement.setInt(10, owner.id());
                var rs = statement.executeQuery();
                rs.next();
                flat = mapRowToEntity(rs);
                connection.commit();
                return flat;
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
            statement.setLong(11, flat.id());
            // saving nested objects
            var owner = userRepository.save(flat.owner());
            var house = houseRepository.save(flat.house());

            statement.setInt(9, house.id());
            statement.setInt(10, owner.id());
            statement.executeUpdate();

            connection.commit();

            return flat;
        }
    }

    @Override
    public List<Flat> findAll() throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.prepareStatement(FIND_ALL);
            var resultSet = statement.executeQuery();
            var list = new ArrayList<Flat>();
            while (resultSet.next()) {
                list.add(mapRowToEntity(resultSet));
            }
            return list;
        }
    }

    public boolean deleteByOwnerId(long id) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            var statement = connection.prepareStatement(DELETE_BY_OWNER_ID);
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }


    @Override
    public Optional<Flat> findById(long id) throws SQLException {
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

        var house = houseRepository.findById(resultSet.getLong("house_id")).orElseThrow(() -> new NotFoundException("House not found"));
        var owner = userRepository.findById(resultSet.getLong("owner_id")).orElseThrow(() -> new NotFoundException("User not found"));

        return new Flat().id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .coordinates(coordinates)
                .createdAt(resultSet.getObject("created_at", OffsetDateTime.class).toZonedDateTime())
                .area(resultSet.getLong("area"))
                .numberOfRooms(resultSet.getLong("number_of_rooms"))
                .furnish(Optional.ofNullable(resultSet.getString("furnish")).map(Furnish::valueOf).orElse(null))
                .view(View.valueOf(resultSet.getString("view")))
                .transport(Optional.ofNullable(resultSet.getString("transport")).map(Transport::valueOf).orElse(null))
                .house(house)
                .owner(owner);
    }
}
