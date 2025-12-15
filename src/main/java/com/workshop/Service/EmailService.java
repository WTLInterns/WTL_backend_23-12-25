package com.workshop.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public boolean sendEmail(String message, String subject, String to) {
    boolean f = false;
    String from = "worldtriplink30@gmail.com"; // Replace with your email
    String host = "smtp.gmail.com";

    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");

    Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("worldtriplink30@gmail.com", "gulk awik boqf mhrr");
        }
    });

    session.setDebug(true);

    try {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(from));

        // ✅ handles multiple recipients
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

        mimeMessage.setSubject(subject);
        mimeMessage.setContent(message, "text/html");

        Transport.send(mimeMessage);
        f = true;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return f;
}

}