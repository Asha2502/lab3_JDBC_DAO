package org.example.dao;

import java.sql.SQLException;
import java.util.List;

public interface AbstractDAO<T> {
    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    int delete(int id) throws SQLException;

    void create(T entity) throws SQLException;
}
