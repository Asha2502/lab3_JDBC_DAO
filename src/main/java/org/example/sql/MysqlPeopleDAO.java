package org.example.sql;


import org.example.dao.PeopleDAO;
import org.example.utils.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlPeopleDAO implements PeopleDAO {
    private static final String SELECT_ALL_PEOPLE = "SELECT * FROM people";
    private static final String SELECT_PEOPLE_BY_ID = "SELECT * FROM people WHERE ID = ?";
    private static final String UPDATE_PERSON = "UPDATE people SET FullName = ?, " +
            "BirthDate = ? WHERE ID = ?";
    private static final String DELETE_PEOPLE = "DELETE FROM people WHERE ID = ?";
    private static final String DELETE_PEOPLE_BY_ENTITY = "DELETE FROM people WHERE FullName = ? " +
            "AND BirthDate = ?";
    private static final String CREATE_PEOPLE = "INSERT INTO people " +
            "(FullName, BirthDate) VALUES (?, ?)";
    private static final String FIND_USER_WITH_SHORTEST_LETTER = """
                SELECT p.ID, p.FullName, p.BirthDate 
                FROM people p JOIN letters l ON p.ID = l.Sender 
                ORDER BY CHAR_LENGTH(l.Text) ASC LIMIT 1
            """;
    private static final String SEND_MESSAGE_TO_ALL_RECIPIENTS = """
                    INSERT INTO letters (Sender, Receiver, Subject, Text, SendDate) 
                    SELECT ?, ID, ?, ?, NOW() FROM people WHERE ID != ?
                """;

    private static final String GET_USERS_WITHOUT_RECEIVED_SUBJECT = """
                    SELECT p.ID, p.FullName, p.BirthDate 
                    FROM people p 
                    WHERE p.ID NOT IN (
                        SELECT Receiver FROM letters WHERE Subject = ?
                    )
                """;
    private static final String GET_USERS_WITH_RECEIVED_SUBJECT = """
                    SELECT DISTINCT p.ID, p.FullName, p.BirthDate 
                    FROM people p 
                    JOIN letters l ON p.ID = l.Receiver 
                    WHERE l.Subject = ?
                """;
    private static final  String GET_USERS_WITH_MESSAGE_COUNTS = """
                    SELECT p.ID, p.FullName, p.BirthDate, 
                        (SELECT COUNT(*) FROM letters WHERE Sender = p.ID) AS SentCount,
                        (SELECT COUNT(*) FROM letters WHERE Receiver = p.ID) AS ReceivedCount
                    FROM people p
                """;

    private final Connection connection;

    public MysqlPeopleDAO() {
        MysqlDAOFactory mysqlDAOFactory = new MysqlDAOFactory();
        this.connection = mysqlDAOFactory.createConnection();
    }

    @Override
    public List<Person> getAll() throws SQLException {
        List<Person> people = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_ALL_PEOPLE)) {
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                people.add(new Person(
                        resultSet.getInt("ID"),
                        resultSet.getString("FullName"),
                        resultSet.getDate("BirthDate")
                ));
            }
        }
        return people;
    }

    @Override
    public Person getById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_PEOPLE_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Person(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate")
                );
            }
        }
        return null;
    }

    @Override
    public int delete(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_PEOPLE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int deleteByEntity(String fullName, Date birthDate) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_PEOPLE_BY_ENTITY)) {
            stmt.setString(1, fullName);
            stmt.setDate(2, birthDate);
            return stmt.executeUpdate();
        }
    }

    @Override
    public void create(Person entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_PEOPLE)) {
            stmt.setString(1, entity.fullName());
            stmt.setDate(2, (Date) entity.birthDate());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Person person) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_PERSON)) {
            stmt.setString(1, person.fullName());
            stmt.setDate(2, (Date) person.birthDate());
            stmt.setInt(3, person.id());
            stmt.executeUpdate();
        }
    }

    @Override
    public Person findUserWithShortestLetter() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(FIND_USER_WITH_SHORTEST_LETTER);
            if (rs.next()) {
                return new Person(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate")
                );
            }
        }
        return null;
    }

    @Override
    public List<Person> getUsersWithMessageCounts() throws SQLException {
        List<Person> people = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(GET_USERS_WITH_MESSAGE_COUNTS);
            while (rs.next()) {
                Person person = new Person(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate")
                );
                people.add(person);
            }
        }
        return people;
    }

    @Override
    public List<Person> getUsersWithReceivedSubject(String subject) throws SQLException {
        List<Person> people = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(GET_USERS_WITH_RECEIVED_SUBJECT)) {
            stmt.setString(1, subject);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                people.add(new Person(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate")
                ));
            }
        }
        return people;
    }

    @Override
    public List<Person> getUsersWithoutReceivedSubject(String subject) throws SQLException {
        List<Person> people = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(GET_USERS_WITHOUT_RECEIVED_SUBJECT)) {
            stmt.setString(1, subject);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                people.add(new Person(
                        rs.getInt("ID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate")
                ));
            }
        }
        return people;
    }

    @Override
    public void sendMessageToAllRecipients(int senderId, String subject, String messageText) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SEND_MESSAGE_TO_ALL_RECIPIENTS)) {
            stmt.setInt(1, senderId);
            stmt.setString(2, subject);
            stmt.setString(3, messageText);
            stmt.setInt(4, senderId);
            stmt.executeUpdate();
        }
    }
}
