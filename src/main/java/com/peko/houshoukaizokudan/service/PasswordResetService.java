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
        MemberDTO userDTO = memberService.findDTOByEmail(email);
        if (userDTO != null) {
            String resetToken = VerificationCodeGenerator.generateCode(6);
            userDTO.setResetToken(resetToken);  // 这里设置resetToken
            memberService.updateMember(userDTO);  // 这里应该会更新数据库记录
        }
    }

    public boolean resetPassword(String email, String resetToken, String newPassword) {
        MemberDTO userDTO = memberService.findDTOByEmail(email);

        if (userDTO != null && resetToken.equals(userDTO.getResetToken())) {
        	System.out.println("resetToken相同");
            String encodedPassword =  passwordEncoder.encode(newPassword);
            System.err.println("新密碼加鹽");
            userDTO.setPasswdbcrypt(encodedPassword);
            System.out.println("設置新密碼");
            userDTO.setResetToken(null); // 清除重置令牌
            memberService.updateMember(userDTO); // 更新用户信息
            return true;
        } else {
        	System.out.println("XX");
            return false;
        }
    }
}
