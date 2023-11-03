package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.service.EmailService;
import com.peko.houshoukaizokudan.service.VerificationCodeGenerator;
import com.peko.houshoukaizokudan.DTO.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/public/api/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String recipient = emailRequest.getRecipient();
        String verificationCode = VerificationCodeGenerator.generateCode(6);
        emailService.sendVerificationEmail(recipient, verificationCode);
        return "驗證碼已發送";
    }

    @PostMapping("/public/api/verifyCode")
    public String verifyCode(@RequestParam String email, @RequestParam String userInputCode) {
        boolean isVerified = emailService.verifyVerificationCode(email, userInputCode);

        if (isVerified) {
            return "驗證成功";
        } else {
            return "驗證失敗";
        }
    }
}
