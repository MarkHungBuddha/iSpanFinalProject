package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String storedVerificationCode;

    public void sendVerificationEmail(String to, String verificationCode) {
        String subject = "驗證您的信箱";
        String content = "您的驗證碼是：" + verificationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);

        storedVerificationCode = verificationCode;
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "重設密碼";
        String content = "您的密碼重設令牌是：" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    public boolean verifyVerificationCode(String receivedCode) {
        if (receivedCode != null && receivedCode.equals(storedVerificationCode)) {
            return true;
        }
        return false;
    }

    public String getStoredVerificationCode() {
        return storedVerificationCode;
    }
}
