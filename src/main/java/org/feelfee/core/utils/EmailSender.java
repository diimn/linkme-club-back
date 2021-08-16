package org.feelfee.core.utils;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailSender {

    public static final String USER_NAME = "linkmeclub@gmail.com";
//    public static final String PASSWORD = "P@ssw0rd13!";
    public static final String PASSWORD = "jazhfxqgtpuponst";

    public void send(String targetEmail, String body) {
        try {
            Session session = getSession();
            Message message = prepareMessage(targetEmail, session, body);
            Transport.send(message);
        } catch (Exception e) {
            System.err.println(e);;
        }
    }

    private Session getSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.localhost", "myHost");

//            prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_NAME, PASSWORD);
            }
        });
    }

    private Message prepareMessage(String targetEmail, Session session, String body) throws MessagingException {
        Message message = new MimeMessage(session);
//        message.setFrom(new InternetAddress(USER_NAME));
        message.setFrom(new InternetAddress("no-reply@linkme.club"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(targetEmail));
        message.setSubject("LinkMe club нотификация");


        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html; charset=UTF-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        return message;
    }

}
