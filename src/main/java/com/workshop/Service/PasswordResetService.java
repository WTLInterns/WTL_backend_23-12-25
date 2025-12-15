package com.workshop.Service;

import com.workshop.Entity.User;
import com.workshop.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Temporary storage for OTPs (email -> OTP)
    private final Map<String, String> otpStorage = new HashMap<>();

    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String otp = generateOTP();
        otpStorage.put(email, otp); // Store OTP in memory

        boolean emailSent = emailService.sendEmail(otp, "Password Reset OTP", email);

        if (emailSent) {
            System.out.println("OTP sent successfully!");
        } else {
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    private String generateOTP() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public boolean validateOTP(String email, String otp) {
        String storedOtp = otpStorage.get(email); // Retrieve OTP from memory
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // Clear OTP after validation
            return true;
        }
        return false;
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword); // Save the hashed password
            userRepository.save(user);
            System.out.println("Password reset successfully.");
        } else {
            throw new RuntimeException("User not found");
        }
    }
}