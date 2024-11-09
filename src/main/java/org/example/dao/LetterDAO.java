package org.example.dao;

import org.example.utils.Letter;

import java.sql.SQLException;


public interface LetterDAO extends AbstractDAO<Letter> {
    int deleteByEntity(Letter entity) throws SQLException;

    void update(Letter entity, int id) throws SQLException;
}
