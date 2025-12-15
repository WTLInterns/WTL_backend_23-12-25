package com.workshop.CarRental.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Repository.CarRentalRepository;

@Service
public class GoogleAuthService {

    @Autowired
    private CarRentalRepository carRentalRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri:https://api.worldtriplink.com/login/oauth2/code/google}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Exchange authorization code for access token and get user info
     */
    public CarRentalUser authenticateWithGoogle(String code) throws IOException {
        // Exchange code for access token
        String accessToken = exchangeCodeForToken(code);
        
        // Get user info from Google
        JsonNode userInfo = getUserInfoFromGoogle(accessToken);
        
        // Extract user details
        String email = userInfo.get("email").asText();
        String name = userInfo.has("name") ? userInfo.get("name").asText() : email.split("@")[0];
        String pictureUrl = userInfo.has("picture") ? userInfo.get("picture").asText() : null;
        
        // Create or update user in database
        return createOrUpdateGoogleUser(email, name, pictureUrl);
    }

    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String code) throws IOException {
        // Validate OAuth configuration
        if (clientId == null || clientId.isEmpty()) {
            throw new IOException("Google OAuth client ID is not configured in application.properties");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new IOException("Google OAuth client secret is not configured in application.properties");
        }
        
        System.out.println("=== Google OAuth Token Exchange Debug ===");
        System.out.println("Client ID: " + (clientId != null ? clientId.substring(0, 20) + "..." : "null"));
        System.out.println("Client Secret: " + (clientSecret != null ? "***configured***" : "null"));
        System.out.println("Redirect URI: " + redirectUri);
        System.out.println("Authorization Code: " + (code != null ? code.substring(0, 20) + "..." : "null"));
        
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            System.out.println("Token exchange successful!");
            
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            System.err.println("Token exchange failed: " + e.getMessage());
            throw new IOException("Failed to exchange authorization code for token. Redirect URI: " + redirectUri + ". Error: " + e.getMessage());
        }
    }

    /**
     * Get user information from Google using access token
     */
    private JsonNode getUserInfoFromGoogle(String accessToken) throws IOException {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUrl, 
                HttpMethod.GET, 
                entity, 
                String.class
        );
        
        return objectMapper.readTree(response.getBody());
    }

    /**
     * Create or update user in database
     */
    public CarRentalUser createOrUpdateGoogleUser(String email, String name, String pictureUrl) {
        Optional<CarRentalUser> existingUser = carRentalRepository.findByEmail(email);
        
        CarRentalUser user;
        if (existingUser.isPresent()) {
            // Update existing user
            user = existingUser.get();
            user.setUserName(name);
        } else {
            // Create new user
            user = new CarRentalUser();
            user.setEmail(email);
            user.setUserName(name);
            user.setRole("USER");
            user.setPassword(null); // No password for OAuth users
            user.setPhone(""); // Phone can be added later
            user.setAddress("");
            user.setGender("");
            user.setLastName("");
            user.setUserlatitude(0.0);
            user.setUserlongitude(0.0);
        }
        
        return carRentalRepository.save(user);
    }

    /**
     * Find user by email
     */
    public Optional<CarRentalUser> findByEmail(String email) {
        return carRentalRepository.findByEmail(email);
    }
}
