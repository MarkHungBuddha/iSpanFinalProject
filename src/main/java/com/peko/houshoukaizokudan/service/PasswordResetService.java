package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.DTO.MemberDTO; // 导入 MemberDTO
import com.peko.houshoukaizokudan.Repository.MemberRepository;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MemberService memberService;

    @Autowired
    private EmailService emailService;

    public void requestPasswordReset(String email) {
        // 根据用户提供的电子邮件查找用户
        MemberDTO userDTO = memberService.findDTOByEmail(email);

        if (userDTO != null) {
            // 生成密码重置令牌
            String resetToken = VerificationCodeGenerator.generateCode(6);

            // 将令牌与用户关联并存储在用户 DTO 对象中
            userDTO.setResetToken(resetToken);

            // 更新用户 DTO 对象
            memberService.updateMember(userDTO);

            // 发送包含令牌的密码重置链接到用户的电子邮件
            emailService.sendPasswordResetEmail(email, resetToken);
        }
    }

    public boolean resetPassword(String email, String resetToken, String newPassword) {
        MemberDTO userDTO = memberService.findDTOByEmail(email);

        if (userDTO != null && resetToken.equals(userDTO.getResetToken())) {
            String encodedPassword =  passwordEncoder.encode(newPassword);
            userDTO.setPasswdbcrypt(encodedPassword);
            userDTO.setResetToken(null); // 清除重置令牌
            memberService.updateMember(userDTO); // 更新用户信息
            return true;
        } else {
            return false;
        }
    }
}
