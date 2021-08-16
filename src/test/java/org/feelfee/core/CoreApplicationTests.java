package org.feelfee.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.feelfee.core.model.Manager;
import org.feelfee.core.utils.EmailSender;
import org.junit.jupiter.api.Test;


class CoreApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        String credentials = "{\"login\" : \"test\", \"password\" : \"Secret13\"} ";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
        Manager manager = objectMapper.readValue(credentials, Manager.class);
        System.out.println(manager.getLogin());
    }

    @Test
    void testSend() {
        EmailSender emailSender = new EmailSender();
        emailSender.send("mr.pavlov.dmitry@gmail.com", "Новый клиент!\n" +
                "Объявление: test-flat\n" +
                "Имя: qwwe232\n" +
                "Телефон: (+4)44_ ___ __ __\n" +
                "Риэлтор: Нет\n");
    }

}
