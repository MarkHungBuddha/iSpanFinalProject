package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.EmailRequest;
import com.peko.houshoukaizokudan.service.EmailService;
import com.peko.houshoukaizokudan.service.VerificationCodeGenerator;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String recipient = emailRequest.getRecipient();

        // 生成验证码，例如：
        String verificationCode = VerificationCodeGenerator.generateCode(6);

        // 发送验证邮件
        emailService.sendVerificationEmail(recipient, verificationCode);

        return "驗證碼已發送";
    }
    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam String userInputCode) {
        boolean isVerified = emailService.verifyVerificationCode(userInputCode);

        if (isVerified) {
            return "驗證成功";
        } else {
            return "驗證失敗";
        }
    }
}
