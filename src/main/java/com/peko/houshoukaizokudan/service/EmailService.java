package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    public void sendVerificationEmail(String to, String verificationCode) {
        String subject = "驗證您的信箱";
        String content = "您的驗證碼是：" + verificationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);

        // Store the code associated with the email address
        verificationCodes.put(to, verificationCode);
    }

    public boolean verifyVerificationCode(String email, String receivedCode) {
        String storedCode = verificationCodes.get(email);
        // Verify that the received code matches the stored code
        return receivedCode != null && receivedCode.equals(storedCode);
    }

    // Optional: Clear the code after verification or timeout
    public void clearVerificationCode(String email) {
        verificationCodes.remove(email);
    }
    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "重置您的密码";
        String content = "您的密码重置码为：" + resetToken + "。请使用此码来重置您的密码。";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        javaMailSender.send(message);
    }
}
