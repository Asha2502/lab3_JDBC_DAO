package org.example.menu;


import org.example.dao.DAOFactory;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuController {
    private final PeopleMethods people;
    private final LetterMethods letter;
    private final PrintStream printStream = System.out;

    public MenuController() {
        DAOFactory mysqlFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        this.people = new PeopleMethods(mysqlFactory);
        this.letter = new LetterMethods(mysqlFactory);
    }

    public void start() throws SQLException {

        printStream.println("\n");
        printStream.println("10. Показать все письма          20. Показать всех людей");
        printStream.println("11. Показать письмо по ID        21. Показать человека по ID");
        printStream.println("12. Создать новое письмо         22. Создать нового человека");
        printStream.println("13. Обновить письмо              23. Обновить данные человека");
        printStream.println("14. Удалить письмо по ID         24. Удалить человека по ID");
        printStream.println("15. Удалить письмо по сущности   25. Удалить человека по сущности\n");

        printStream.println("30. Найти пользователя с самым коротким письмом");
        printStream.println("31. Вывести информацию о пользователях, а также количестве полученных и " +
                "отправленных ими письмах");
        printStream.println("32. Вывести информацию о пользователях, которые получили хотя бы одно сообщение " +
                "с заданной темой");
        printStream.println("33. Вывести информацию о пользователях, которые не получали сообщения с заданной " +
                "темой");
        printStream.println("34. Направить письмо заданного человека с заданной темой всем адресатам");
        printStream.println("0. Выход");

        while (true) {
            printStream.println("\nВыберите действие:");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 10 -> letter.showAllLetters();
                case 11 -> letter.showLetterById(scanner);
                case 12 -> letter.createLetter(scanner);
                case 13 -> letter.updateLetter(scanner);
                case 14 -> letter.deleteLetterById(scanner);
                case 15 -> letter.deleteLetterByEntity(scanner);

                case 20 -> people.showAllPeople();
                case 21 -> people.showPersonById(scanner);
                case 22 -> people.createPerson(scanner);
                case 23 -> people.updatePerson(scanner);
                case 24 -> people.deletePersonById(scanner);
                case 25 -> people.deletePersonByEntity(scanner);

                case 30 -> people.findUserWithShortestLetter();
                case 31 -> people.getUsersWithMessageCounts();
                case 32 -> people.usersWithReceivedSubject(scanner);
                case 33 -> people.usersWithoutReceivedSubject(scanner);
                case 34 -> people.sendMessageToRecipients(scanner);

                case 0 -> {
                    scanner.close();
                    return;
                }
                default -> printStream.println("Неверный выбор, попробуйте снова.");
            }
        }
    }


}
