package org.example.utils;


import java.util.Date;

public record Person(
        int id,
        String fullName,
        Date birthDate) {
}
