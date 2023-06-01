package se.ifmo.lab07.persistance.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T save(T entity) throws SQLException;

    Optional<T> findById(long id) throws SQLException;

    boolean deleteById(long id) throws SQLException;

    List<T> findAll() throws SQLException;

}
