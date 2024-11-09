package org.example.menu;


import org.example.dao.DAOFactory;
import org.example.dao.PeopleDAO;
import org.example.utils.Person;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PeopleMethods {
    private final PrintStream printStream = System.out;
    private final PeopleDAO peopleDAO;

    public PeopleMethods(DAOFactory mysqlFactory) {
        this.peopleDAO = mysqlFactory.getPeopleDAO();
    }

    public void showAllPeople() throws SQLException {
        List<Person> people = peopleDAO.getAll();
        people.forEach(System.out::println);
    }

    public void showPersonById(Scanner scanner) throws SQLException {
        printStream.print("Введите ID человека: ");
        int id = scanner.nextInt();
        Person person = peopleDAO.getById(id);
        printStream.println(person != null ? person : "Человек не найден.");
    }

    public void createPerson(Scanner scanner) throws SQLException {
        printStream.print("Введите полное имя: ");
        String fullName = scanner.nextLine();
        printStream.print("Введите дату рождения (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();
        Person person = new Person(0, fullName, java.sql.Date.valueOf(birthDate));
        peopleDAO.create(person);
        printStream.println("Человек создан.");
    }

    public void updatePerson(Scanner scanner) throws SQLException {
        printStream.print("Введите ID человека для обновления: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        printStream.print("Введите новое полное имя: ");
        String fullName = scanner.nextLine();
        printStream.print("Введите новую дату рождения (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();
        Person person = new Person(id, fullName, java.sql.Date.valueOf(birthDate));
        peopleDAO.update(person);
        printStream.println("Человек обновлен.");
    }

    public void deletePersonById(Scanner scanner) throws SQLException {
        printStream.print("Введите ID человека для удаления: ");
        int id = scanner.nextInt();
        int rowsAffected = peopleDAO.delete(id);
        if (rowsAffected > 0) {
            printStream.println("Человек удален.");
        } else {
            printStream.println("Человек не найден в базе данных.");
        }

    }

    public void deletePersonByEntity(Scanner scanner) throws SQLException {
        printStream.print("Введите имя и фамилию человека для удаления: ");
        String fullName = scanner.nextLine();
        printStream.print("Введите дату рождения (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();

        int rowsAffected = peopleDAO.deleteByEntity(fullName, java.sql.Date.valueOf(birthDate));
        if (rowsAffected > 0) {
            printStream.println("Человек удален.");
        } else {
            printStream.println("Человек не найден в базе данных.");
        }
    }

    public void findUserWithShortestLetter() throws SQLException {
        Person person = peopleDAO.findUserWithShortestLetter();
        printStream.println(person != null ? person : "Пользователь не найден.");
    }

    public void getUsersWithMessageCounts() throws SQLException {
        List<Person> people = peopleDAO.getUsersWithMessageCounts();
        people.forEach(System.out::println);
    }

    public void usersWithReceivedSubject(Scanner scanner) throws SQLException {
        printStream.print("Введите тему: ");
        String subject = scanner.nextLine();
        List<Person> people = peopleDAO.getUsersWithReceivedSubject(subject);
        people.forEach(System.out::println);
    }

    public void usersWithoutReceivedSubject(Scanner scanner) throws SQLException {
        printStream.print("Введите тему: ");
        String subject = scanner.nextLine();
        List<Person> people = peopleDAO.getUsersWithoutReceivedSubject(subject);
        people.forEach(System.out::println);
    }

    public void sendMessageToRecipients(Scanner scanner) throws SQLException {
        printStream.print("Введите ID человека, от которого будет рассылка: ");
        int senderId = scanner.nextInt();
        scanner.nextLine();
        printStream.print("Введите тему: ");
        String subject = scanner.nextLine();
        printStream.print("Введите текст сообщения: ");
        String text = scanner.nextLine();
        peopleDAO.sendMessageToAllRecipients(senderId, subject, text);
        System.out.println("Письмо отправлено.");
    }
}
