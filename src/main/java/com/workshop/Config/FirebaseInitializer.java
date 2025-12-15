package com.workshop.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials credentials = null;
            
            // Try environment variable first (Base64 encoded)
            String firebaseCredentialsBase64 = System.getenv("FIREBASE_SERVICE_ACCOUNT_BASE64");
            if (firebaseCredentialsBase64 != null && !firebaseCredentialsBase64.isEmpty()) {
                System.out.println("🔥 Loading Firebase credentials from environment variable");
                byte[] credentialsBytes = Base64.getDecoder().decode(firebaseCredentialsBase64);
                credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsBytes));
            } else {
                // Try loading from classpath resource
                try {
                    System.out.println("🔥 Loading Firebase credentials from classpath: /serviceAccountKey.json");
                    var resourceStream = getClass().getResourceAsStream("/serviceAccountKey.json");
                    if (resourceStream != null) {
                        credentials = GoogleCredentials.fromStream(resourceStream);
                        System.out.println("✅ Firebase credentials loaded successfully from classpath");
                    } else {
                        System.err.println("❌ Failed to load Firebase from classpath: /serviceAccountKey.json");
                        System.err.println("❌ Firebase initialization failed - no valid credentials found");
                        System.err.println("❌ Please ensure either:");
                        System.err.println("   1. Set FIREBASE_SERVICE_ACCOUNT_BASE64 environment variable");
                        System.err.println("   2. Place serviceAccountKey.json in src/main/resources/");
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error loading Firebase credentials from classpath: " + e.getMessage());
                    return;
                }
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully!");
            } else {
                System.out.println("✅ Firebase already initialized");
            }

        } catch (IOException e) {
            System.err.println("❌ Firebase initialization failed with IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed with unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
