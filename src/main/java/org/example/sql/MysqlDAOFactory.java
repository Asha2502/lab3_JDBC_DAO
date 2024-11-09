package org.example.sql;



import org.example.dao.DAOFactory;
import org.example.dao.LetterDAO;
import org.example.dao.PeopleDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlDAOFactory extends DAOFactory {
    private static final String PROPERTIES_FILE = "db.properties";
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASS;

    static {
        try (InputStream input = MysqlDAOFactory.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Извините, не удалось найти " + PROPERTIES_FILE);
            }
            properties.load(input);

            DRIVER = properties.getProperty("db.driver");
            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASS = properties.getProperty("db.password");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
            conn.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    @Override
    public LetterDAO getLetterDAO() {
        return new MysqlLetterDAO();
    }

    @Override
    public PeopleDAO getPeopleDAO() {
        return new MysqlPeopleDAO();
    }
}