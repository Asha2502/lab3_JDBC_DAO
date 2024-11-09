package org.example.menu;


import org.example.dao.DAOFactory;
import org.example.dao.LetterDAO;
import org.example.utils.Letter;

import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class LetterMethods {

    private final PrintStream printStream = System.out;
    private final LetterDAO letterDAO;

    public LetterMethods(DAOFactory mysqlFactory) {
        this.letterDAO = mysqlFactory.getLetterDAO();
    }

    public void showAllLetters() throws SQLException {
        List<Letter> letters = letterDAO.getAll();
        letters.forEach(System.out::println);
    }

    public void showLetterById(Scanner scanner) throws SQLException {
        printStream.print("Введите ID письма: ");
        int id = scanner.nextInt();
        Letter letter = letterDAO.getById(id);
        printStream.println(letter != null ? letter : "Письмо не найдено.");
    }

    public void createLetter(Scanner scanner) throws SQLException {
        System.out.print("Введите ID отправителя: ");
        int senderId = scanner.nextInt();
        System.out.print("Введите ID получателя: ");
        int receiverId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Введите тему: ");
        String subject = scanner.nextLine();
        System.out.print("Введите текст письма: ");
        String text = scanner.nextLine();

        Letter letter = new Letter(0, senderId, receiverId, subject, text, new Timestamp(System.currentTimeMillis()));
        letterDAO.create(letter);
        System.out.println("Письмо создано.");
    }

    public void updateLetter(Scanner scanner) throws SQLException {
        printStream.print("Введите ID письма для обновления: ");
        int id = scanner.nextInt();
        printStream.print("Введите новый ID отправителя: ");
        int senderId = scanner.nextInt();
        printStream.print("Введите новый ID получателя: ");
        int receiverId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        printStream.print("Введите новую тему: ");
        String subject = scanner.nextLine();
        printStream.print("Введите новый текст письма: ");
        String text = scanner.nextLine();

        Letter letter = new Letter(senderId, receiverId, subject, text, new Timestamp(System.currentTimeMillis()));
        letterDAO.update(letter, id);
        printStream.println("Письмо обновлено.");
    }

    public void deleteLetterById(Scanner scanner) throws SQLException {
        printStream.print("Введите ID письма для удаления: ");
        int id = scanner.nextInt();
        int rowsAffected = letterDAO.delete(id);
        if (rowsAffected > 0) {
            printStream.println("Письмо удалено.");
        } else {
            printStream.println("Письмо не найдено в базе данных.");
        }
    }

    public void deleteLetterByEntity(Scanner scanner) throws SQLException {
        printStream.print("Введите ID отправителя: ");
        int senderId = scanner.nextInt();
        printStream.print("Введите ID получателя: ");
        int receiverId = scanner.nextInt();
        scanner.nextLine();
        printStream.print("Введите тему письма: ");
        String subject = scanner.nextLine();
        printStream.print("Введите текст письма: ");
        String text = scanner.nextLine();
        printStream.print("Введите дату и время отправки (yyyy-MM-dd hh:mm:ss): ");
        Timestamp sendDate = Timestamp.valueOf(scanner.nextLine());
        Letter letter = new Letter(senderId, receiverId, subject, text, sendDate);
        int rowsAffected = letterDAO.deleteByEntity(letter);
        if (rowsAffected > 0) {
            printStream.println("Письмо удалено.");
        } else {
            printStream.println("Письмо не найдено в базе данных.");
        }
    }
}
