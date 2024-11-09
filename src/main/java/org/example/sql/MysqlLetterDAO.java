package org.example.sql;


import org.example.dao.LetterDAO;
import org.example.utils.Letter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MysqlLetterDAO implements LetterDAO {
    private static final String SELECT_ALL_LETTERS = "SELECT * FROM letters";
    private static final String SELECT_LETTER_BY_ID = "SELECT * FROM letters WHERE ID=?";
    private static final String UPDATE_LETTER = "UPDATE letters SET Sender = ?, Receiver = ?, " +
            "Subject = ?, Text = ?, SendDate = ? WHERE ID = ?";
    private static final String DELETE_LETTER = "DELETE FROM letters WHERE ID = ?";
    private static final String DELETE_LETTER_BY_ENTITY = "DELETE FROM letters WHERE Sender = ? " +
            "AND Receiver = ? AND Subject = ? AND Text = ? AND SendDate = ?";
    private static final String CREATE_LETTER = "INSERT INTO letters " +
            "(Sender, Receiver, Subject, Text, SendDate) VALUES (?, ?, ?, ?, ?)";

    private final Connection connection;

    public MysqlLetterDAO() {
        MysqlDAOFactory mysqlDAOFactory = new MysqlDAOFactory();
        this.connection = mysqlDAOFactory.createConnection();
    }

    @Override
    public List<Letter> getAll() throws SQLException {
        List<Letter> letters = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_LETTERS)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Letter letter = new Letter(
                        resultSet.getInt("ID"),
                        resultSet.getInt("Sender"),
                        resultSet.getInt("Receiver"),
                        resultSet.getString("Subject"),
                        resultSet.getString("Text"),
                        resultSet.getTimestamp("SendDate")
                );
                letters.add(letter);
            }
        }
        return letters;
    }

    @Override
    public Letter getById(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_LETTER_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return new Letter(
                        resultSet.getInt("ID"),
                        resultSet.getInt("Sender"),
                        resultSet.getInt("Receiver"),
                        resultSet.getString("Subject"),
                        resultSet.getString("Text"),
                        resultSet.getTimestamp("SendDate")
                );
            }
        }
        return null;
    }

    @Override
    public int delete(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_LETTER)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int deleteByEntity(Letter entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_LETTER_BY_ENTITY)) {
            stmt.setInt(1, entity.getSenderId());
            stmt.setInt(2, entity.getReceiverId());
            stmt.setString(3, entity.getSubject());
            stmt.setString(4, entity.getText());
            stmt.setTimestamp(5, entity.getSendDate());
            return stmt.executeUpdate();
        }
    }

    @Override
    public void create(Letter letter) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_LETTER)) {
            stmt.setInt(1, letter.getSenderId());
            stmt.setInt(2, letter.getReceiverId());
            stmt.setString(3, letter.getSubject());
            stmt.setString(4, letter.getText());
            stmt.setTimestamp(5, letter.getSendDate());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Letter letter, int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_LETTER)) {
            stmt.setInt(1, letter.getSenderId());
            stmt.setInt(2, letter.getReceiverId());
            stmt.setString(3, letter.getSubject());
            stmt.setString(4, letter.getText());
            stmt.setTimestamp(5, letter.getSendDate());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

}
