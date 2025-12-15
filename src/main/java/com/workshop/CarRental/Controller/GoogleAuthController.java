package com.workshop.CarRental.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Service.GoogleAuthService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;

@RestController
@CrossOrigin("*")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String clientId;

    @Value("${app.frontend.url:https://worldtriplink.com}")
    private String frontendUrl;

    /**
     * Endpoint to initiate Google OAuth login
     * Frontend will redirect to this URL
     */
    @GetMapping("/auth/google/login")
    public void googleLogin(HttpServletResponse response) throws IOException {
        System.out.println("=== Google OAuth Login Initiated ===");
        System.out.println("Client ID loaded: " + (clientId != null && !clientId.isEmpty() ? "Yes (" + clientId.substring(0, 20) + "...)" : "NO - EMPTY!"));
        System.out.println("Frontend URL: " + frontendUrl);
        
        // Check if OAuth is configured
        if (clientId == null || clientId.isEmpty()) {
            System.err.println("ERROR: Client ID is not configured!");
            String errorUrl = frontendUrl + "/login?googleAuth=error&message=Google OAuth is not configured. Please add OAuth credentials to application.properties";
            response.sendRedirect(errorUrl);
            return;
        }
        
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=https://api.worldtriplink.com/login/oauth2/code/google" +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&access_type=offline" +
                "&prompt=consent";
        
        System.out.println("Redirecting to Google: " + googleAuthUrl.substring(0, 100) + "...");
        response.sendRedirect(googleAuthUrl);
    }

    /**
     * OAuth2 callback endpoint - matches Google Console redirect URI
     * Google redirects here after user authentication
     */
    @GetMapping("/login/oauth2/code/google")
    public void googleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response) throws IOException {
        
        System.out.println("=== Google OAuth Callback Received ===");
        System.out.println("Authorization code received: " + (code != null ? "Yes" : "No"));
        
        try {
            // Exchange authorization code for user info
            CarRentalUser user = googleAuthService.authenticateWithGoogle(code);
            
            System.out.println("User authenticated successfully: " + user.getEmail());
            
            // Redirect to frontend with user data as query parameters
            String redirectUrl = frontendUrl + "/login?googleAuth=success" +
                    "&userId=" + user.getId() +
                    "&username=" + user.getUserName() +
                    "&email=" + user.getEmail() +
                    "&phone=" + (user.getPhone() != null ? user.getPhone() : "") +
                    "&role=" + user.getRole();
            
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            System.err.println("OAuth callback error: " + e.getMessage());
            e.printStackTrace();
            
            // Redirect to frontend with error
            String errorMessage = e.getMessage();
            if (errorMessage.contains("401")) {
                errorMessage = "Google authentication failed. This may be due to: 1) Invalid OAuth credentials, 2) Redirect URI mismatch, or 3) Authorization code already used (try signing in again)";
            }
            String redirectUrl = frontendUrl + "/login?googleAuth=error&message=" + errorMessage;
            response.sendRedirect(redirectUrl);
        }
    }

    /**
     * Alternative endpoint for token-based authentication
     * Frontend sends Google ID token directly
     */
    @PostMapping("/auth/google/verify-token")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");
        
        if (idTokenString == null || idTokenString.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "ID token is required"));
        }

        try {
            // Verify the Google ID token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), 
                    new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                
                // Extract user information
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                
                // Create or update user
                CarRentalUser user = googleAuthService.createOrUpdateGoogleUser(email, name, pictureUrl);
                
                // Return user data
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("userId", user.getId());
                responseData.put("username", user.getUserName());
                responseData.put("email", user.getEmail());
                responseData.put("phone", user.getPhone());
                responseData.put("role", user.getRole());
                responseData.put("address", user.getAddress());
                responseData.put("isLoggedIn", true);
                
                return ResponseEntity.ok(responseData);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid ID token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Token verification failed: " + e.getMessage()));
        }
    }

    /**
     * Get Google OAuth configuration for frontend
     */
    @GetMapping("/auth/google/config")
    public ResponseEntity<?> getGoogleConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("clientId", clientId);
        config.put("redirectUri", "https://api.worldtriplink.com/auth/google/callback");
        return ResponseEntity.ok(config);
    }
}
