//package com.peko.houshoukaizokudan.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class VerificationController {
//
//    @GetMapping("/verify-verification-code")
//    public String verifyVerificationCode(@RequestParam String userInputCode, HttpSession session) {
//        // 获取存储在会话中的验证码
//        String verificationCode = (String) session.getAttribute("verificationCode");
//
//        if (verificationCode == null) {
//            // 会话中没有验证码
//            return "验证码不存在，请重新生成验证码。";
//        }
//
//        // 检查用户输入的验证码是否与存储在会话中的验证码匹配
//        if (verificationCode.equals(userInputCode)) {
//            // 验证成功
//            return "验证成功！";
//        } else {
//            // 验证失败
//            return "验证失败，请检查输入的验证码。";
//        }
//    }
//}
