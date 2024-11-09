package org.example.dao;


import org.example.sql.MysqlDAOFactory;

public abstract class DAOFactory {
    public static final int MYSQL = 0;

    public abstract LetterDAO getLetterDAO();

    public abstract PeopleDAO getPeopleDAO();

    public static DAOFactory getDAOFactory(int database) {
        if (database == MYSQL) {
            return new MysqlDAOFactory();
        }
        return null;
    }
}
