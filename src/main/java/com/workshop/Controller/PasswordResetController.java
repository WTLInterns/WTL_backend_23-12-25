package com.workshop.Controller;

import com.workshop.Service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request-reset")
    public String sendPasswordResetEmail(@RequestParam String email) {
        passwordResetService.sendPasswordResetEmail(email);
        return "OTP sent successfully!";
    }

    @PostMapping("/verify-otp")
    public boolean verifyOTP(@RequestParam String email, @RequestParam String otp) {
        return passwordResetService.validateOTP(email, otp);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        passwordResetService.resetPassword(email, newPassword);
        return "Password reset successfully.";
    }
}