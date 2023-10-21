package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.model.EmailRequest;
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
}
