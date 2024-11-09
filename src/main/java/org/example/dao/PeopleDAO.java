package org.example.dao;



import org.example.utils.Person;

import java.sql.SQLException;
import java.util.List;

public interface PeopleDAO extends AbstractDAO<Person> {

    int deleteByEntity(String fullName, java.sql.Date birthDate) throws SQLException;

    void update(Person entity) throws SQLException;

    Person findUserWithShortestLetter() throws SQLException;

    List<Person> getUsersWithMessageCounts() throws SQLException;

    List<Person> getUsersWithReceivedSubject(String subject) throws SQLException;

    List<Person> getUsersWithoutReceivedSubject(String subject) throws SQLException;

    void sendMessageToAllRecipients(int senderId, String subject, String messageText) throws SQLException;
}