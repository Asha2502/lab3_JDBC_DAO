package org.example.utils;

import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter @ToString
public class Letter {
    private int letterId;
    private final int senderId;
    private final int receiverId;
    private final String subject;
    private final String text;
    private final Timestamp sendDate;

    public Letter(
            int senderId,
            int receiverId,
            String subject,
            String text,
            Timestamp sendDate
    ) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        this.text = text;
        this.sendDate = sendDate;
    }

    public Letter(int letterId, int senderId, int receiverId, String subject, String text, Timestamp sendDate) {
        this.letterId = letterId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        this.text = text;
        this.sendDate = sendDate;
    }
}
