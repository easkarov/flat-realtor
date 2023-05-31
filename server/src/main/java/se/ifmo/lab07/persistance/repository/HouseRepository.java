package se.ifmo.lab07.persistance.repository;

import se.ifmo.lab07.entity.House;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class HouseRepository implements Repository<House> {

    private static final String URL = "jdbc:postgresql://localhost:5432/lab07";

    private static final String USER = "postgres";

    private static final String PASSWORD = "password";

    private static final String UPDATE_SQL = """
            UPDATE house SET name = ?, year = ?, number_of_flats = ? WHERE id = ?;
            """;

    private static final String SAVE_SQL = """
            WITH h AS (INSERT INTO house(name, year, number_of_flats) values (?, ?, ?)
            RETURNING id, name, year, number_of_flats)
            SELECT * FROM h;
            """;

    private static final String FIND_BY_ID = """
            SELECT * FROM house WHERE id = ?;
            """;

    private static final String DELETE_BY_ID = """
            DELETE FROM house WHERE id = ?;
            """;

    @Override
    public House save(House house) throws SQLException {
        try (var connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (house.id() == null) {
                var statement = connection.prepareStatement(SAVE_SQL);
                statement.setString(1, house.name());
                statement.setLong(2, house.year());
                statement.setInt(3, house.numberOfFlatsOnFloor());
                return mapRowToEntity(statement.executeQuery());
            }
            var statement = connection.prepareStatement(UPDATE_SQL);
            statement.setString(1, house.name());
            statement.setLong(2, house.year());
            statement.setInt(3, house.numberOfFlatsOnFloor());
            statement.setInt(4, house.id());
            statement.executeUpdate();
            return house;
        }
    }

    @Override
    public Optional<House> getById(long id) throws SQLException {
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

    private House mapRowToEntity(ResultSet resultSet) throws SQLException {
        return new House()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .year(resultSet.getLong("year"))
                .numberOfFlatsOnFloor(resultSet.getInt("number_of_flats"));
    }
}
