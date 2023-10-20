package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.model.EmailRequest;
import com.peko.houshoukaizokudan.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // 使用 POST 请求方法
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        String recipient = emailRequest.getRecipient();
        String subject = "您的验证码";
        String content = "验证码是：" + emailRequest.getContent();

        emailService.sendVerificationEmail(recipient, emailRequest.getContent());

        return "验证码已发送";
    }
}