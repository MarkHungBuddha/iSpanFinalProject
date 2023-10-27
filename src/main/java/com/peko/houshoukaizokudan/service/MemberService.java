package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.Repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private MemberRepository usersRepo;

    @Autowired
    private EmailService emailService;

    public Member addUser(Member users) {
        String encodedPwd = pwdEncoder.encode(users.getPasswdbcrypt()); // 加密
        users.setPasswdbcrypt(encodedPwd);
        return usersRepo.save(users);
    }

    public boolean checkIfUsernameExist(String username) {
        Member dbUser = usersRepo.findByUsername(username);
        return dbUser != null;
    }

    public Member checkLogin(String username, String inputPwd) {
        Member dbUser = usersRepo.findByUsername(username);

        if (dbUser != null) {
            if (pwdEncoder.matches(inputPwd, dbUser.getPasswdbcrypt())) {
                return dbUser;
            }
        }

        return null;
    }

    public void sendVerificationEmail(String email, String verificationCode) {
        emailService.sendVerificationEmail(email, verificationCode);
    }

    public Member findById(Integer id) {
        return usersRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + id + " 的會員"));
    }
    public Member findByEmail(String email) {
        Member member = usersRepo.findByEmail(email);
        if (member == null) {
            throw new RuntimeException("找不到 " + email + " 的會員");
        }
        return member;
    }
    public Member updateMember(Member member) {
        return usersRepo.save(member);
    }
    public void deleteMember(Integer memberId) {
        usersRepo.deleteById(memberId);
    }
    public Member findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }
    
    
}
