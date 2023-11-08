package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.SmsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private MemberService userUservice;

    @PostMapping("/customer/api/sendPhoneVCode")
    public ResponseEntity<String> sendVerificationCode(
            @RequestParam String mobile,
            @RequestParam Integer userId) { // 假设前端会提供用户ID
        try {
            smsService.sendPhoneVCode(mobile, userId);
            return ResponseEntity.ok("驗證碼已成功送出");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("驗證碼送出失敗: " + e.getMessage());
        }
    }

    // 验证接收到的验证码
    @PostMapping("/customer/api/PhoneVCode")
    public ResponseEntity<String> verifyVerificationCode(
            @RequestParam String mobile,
            @RequestParam String verificationCode,
            HttpSession httpsession) {
        boolean isVerified = smsService.verifyVerificationCode(mobile, verificationCode);
        if (isVerified) {
            Member loggedInUser = (Member) httpsession.getAttribute("loginUser");
            Member result = userUservice.findById(loggedInUser.getId());
            httpsession.removeAttribute("loginUser");
            httpsession.setAttribute("loginUser", result);
            return ResponseEntity.ok("驗證成功");
        } else {
            return ResponseEntity.badRequest().body("驗證失敗");
        }
    }
}