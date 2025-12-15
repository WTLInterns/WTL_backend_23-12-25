package com.workshop.Service;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public boolean sendSms(String phoneNumber, String carrier, String message) {
        boolean f = false;

        // Use email-to-SMS gateway
        String to = constructSmsGatewayAddress(phoneNumber, carrier);

        // Email settings
        String from = "jaywantmhala928@gmail.com"; // Replace with your email
        String host = "smtp.gmail.com";

        // Set up properties for the mail session
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", 465); // Use 465 for SSL
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.auth", true);

        // Create a session with email credentials (username and password)
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jaywantmhala928@gmail.com", "mcjd ebsg arvj ftca"); // Replace with actual app password
            }
        });

        session.setDebug(true);

        // Create a MimeMessage for the email
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject("SMS");
            mimeMessage.setText(message); // SMS content for the message body

            // Send the email (which will be received as an SMS)
            Transport.send(mimeMessage);
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // Method to construct the email-to-SMS gateway address
    private String constructSmsGatewayAddress(String phoneNumber, String carrier) {
        switch (carrier.toLowerCase()) {
            case "verizon":
                return phoneNumber + "@vtext.com";
            case "t-mobile":
                return phoneNumber + "@tmomail.net";
            case "att":
                return phoneNumber + "@txt.att.net";
            case "sprint":
                return phoneNumber + "@messaging.sprintpcs.com";
            default:
                throw new IllegalArgumentException("Unsupported carrier: " + carrier);
        }
    }
}