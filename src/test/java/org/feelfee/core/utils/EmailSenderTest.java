package org.feelfee.core.utils;

import org.junit.jupiter.api.Test;

public class EmailSenderTest {

    @Test
    void testSend() {
        EmailSender emailSender = new EmailSender();
        emailSender.send("mr.pavlov.dmitry@gmail.com", "TEST");
    }
}
