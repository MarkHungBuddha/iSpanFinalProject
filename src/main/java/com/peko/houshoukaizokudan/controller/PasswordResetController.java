package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.service.PasswordResetService;

@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request")
    public String requestPasswordReset(@RequestParam String email) {
        try {
            passwordResetService.requestPasswordReset(email);
            return "Password reset email sent successfully.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String email, @RequestParam String resetToken, @RequestParam String newPassword) {
        try {
            boolean success = passwordResetService.resetPassword(email, resetToken, newPassword);
            if (success) {
                return "Password reset successful.";
            } else {
                return "Password reset failed. Please check your email and token.";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
