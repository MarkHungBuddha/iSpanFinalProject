package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.service.PasswordResetService;

@RestController
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/public/api/request")
    public String requestPasswordReset(@RequestParam String email) {
        try {
            passwordResetService.requestPasswordReset(email);
            return "驗證信已寄出";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/public/api/reset")
    public String resetPassword(@RequestParam String email, @RequestParam String resetToken, @RequestParam String newPassword) {
        try {
            boolean success = passwordResetService.resetPassword(email, resetToken, newPassword);
            if (success) {
                return "密碼更改成功";
            } else {
                return "更改失敗，請檢查驗證碼與密碼格式";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
