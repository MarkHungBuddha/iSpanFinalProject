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
        String subject = "验证您的邮箱";
        String content = "您的验证码是：" + verificationCode;
        
        
        
//        // 包含验证令牌参数
//        content += "\n请点击以下链接来验证您的邮箱：https://yourapp.com/email/verify?token=" + verificationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
        
        storedVerificationCode = verificationCode;
    }
    public boolean verifyVerificationCode(String receivedCode, String storedCode) {
        if (receivedCode != null && receivedCode.equals(storedCode)) {
            // 验证通过
            return true;
        }
        // 验证失败
        return false;
    }
    public String getStoredVerificationCode() {
        return storedVerificationCode;
    }
}
