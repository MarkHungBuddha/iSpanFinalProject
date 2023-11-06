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
            userDTO.setResetToken(resetToken);  // 这里设置resetToken
            memberService.updateMember(userDTO);  // 这里应该会更新数据库记录
        }
    }

    public boolean resetPassword(String email, String resetToken, String newPassword) {
        MemberDTO userDTO = memberService.findDTOByEmail(email);

        if (userDTO != null && resetToken.equals(userDTO.getResetToken())) {
//        	System.out.println("resetToken相同");
//        	System.err.println("新密碼:"+newPassword);
            String encodedPassword =  passwordEncoder.encode(newPassword);
//            if(encodedPassword != null) {
//            System.err.println("新密碼加鹽");}
            userDTO.setPasswdbcrypt(encodedPassword);
            System.out.println("設置新密碼");
//            userDTO.setResetToken(null); // 清除重置令牌
            memberService.updateMember(userDTO); // 更新用户信息
//            if(passwordEncoder.matches(newPassword, encodedPassword)) {
//            	System.out.println("有");
//            }else {
//            	System.out.println("NO");
//            }
//            if(userDTO.getPasswdbcrypt() == encodedPassword) {
//            	System.out.println("正確");
//            }else {
//            	System.out.println("Nono");
//            }
            return true;
        } else {
        	System.out.println("XX");
            return false;
        }
    }
}