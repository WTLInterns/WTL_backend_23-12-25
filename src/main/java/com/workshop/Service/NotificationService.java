package com.workshop.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Firebase Admin SDK
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Value("${fcm.server.key:}")
    private String fcmServerKey; // Set in application.properties as fcm.server.key=YOUR_SERVER_KEY or ENV

    private static final String FCM_SEND_URL = "https://fcm.googleapis.com/fcm/send";

    public boolean sendToToken(String token, String title, String body, Map<String, String> data) {
        if (token == null || token.isBlank()) return false;
        if (fcmServerKey == null || fcmServerKey.isBlank()) {
            // Try Firebase Admin SDK (OAuth via service account), no legacy server key needed
            try {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build();

                Message.Builder builder = Message.builder()
                        .setToken(token)
                        .setNotification(notification);

                if (data != null && !data.isEmpty()) {
                    builder.putAllData(data);
                }

                Message message = builder.build();
                String response = FirebaseMessaging.getInstance().send(message);
                String tokenPrefix = token.substring(0, Math.min(12, token.length()));
                System.out.println("[AdminSDK][FCM] Sent. tokenPrefix=" + tokenPrefix + " title='" + (title == null ? "" : title) + "' response=" + response);
                return true;
            } catch (FirebaseMessagingException e) {
                String tokenPrefix = token.substring(0, Math.min(12, token.length()));
                System.out.println("[AdminSDK][FCM] Send failed. tokenPrefix=" + tokenPrefix + " errorCode=" + e.getErrorCode() + " message=" + e.getMessage());
                return false;
            } catch (Exception ex) {
                System.err.println("[AdminSDK][FCM] Send failed: " + ex.getMessage());
                return false;
            }
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("to", token);

            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", body);
            notification.put("sound", "default");
            payload.put("notification", notification);

            if (data != null && !data.isEmpty()) {
                payload.put("data", data);
            }

            // Debug log: outgoing payload overview
            String tokenPrefix = token.substring(0, Math.min(12, token.length()));
            System.out.println("[HTTP][FCM] Preparing send → tokenPrefix=" + tokenPrefix
                    + " title='" + (title == null ? "" : title) + "'"
                    + " bodyLen=" + (body == null ? 0 : body.length())
                    + " dataKeys=" + (data == null ? 0 : data.keySet().size()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "key=" + fcmServerKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(FCM_SEND_URL, entity, String.class);
            System.out.println("[HTTP][FCM] Sent. tokenPrefix=" + tokenPrefix + " title='" + title + "' bodyLen=" + (body==null?0:body.length()) + " response=" + response);
            return true;
        } catch (Exception ex) {
            String tokenPrefix = token == null ? "null" : token.substring(0, Math.min(12, token.length()));
            System.err.println("[HTTP][FCM] Send failed. tokenPrefix=" + tokenPrefix + " error=" + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
