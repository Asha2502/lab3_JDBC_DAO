package org.example;

import org.example.menu.MenuController;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        new MenuController().start();
    }

}
