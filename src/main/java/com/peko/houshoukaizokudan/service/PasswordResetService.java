//package com.peko.houshoukaizokudan.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.peko.houshoukaizokudan.model.Member;
//
//@Service
//public class PasswordResetService {
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private EmailService emailService;
//
//    public void requestPasswordReset(String email) {
//        // 根据用户提供的电子邮件查找用户
//        Member user = memberService.findByEmail(email);
//
//        if (user != null) {
//            // 生成密码重置令牌
//            String resetToken = VerificationCodeGenerator.generateCode(6);
//
//            // 将令牌与用户关联并存储在用户对象中
//            user.setResetToken(resetToken);
//
//            // 更新用户对象
//            memberService.updateMember(user);
//
//            // 发送包含令牌的密码重置链接到用户的电子邮件
//            emailService.sendPasswordResetEmail(email, resetToken);
//        }
//    }
//
//    public boolean resetPassword(String email, String resetToken, String newPassword) {
//        Member user = memberService.findByEmail(email);
//
//        if (user != null && resetToken.equals(user.getResetToken())) {
//            String encodedPassword = yourCustomPasswordEncryptMethod(newPassword);
//            user.setPasswdbcrypt(encodedPassword);
//            user.setResetToken(null); // 清除重置令牌
//            memberService.updateMember(user); // 更新用户信息
//            return true;
//        } else {
//            return false;
//        }
//    }
//    }
