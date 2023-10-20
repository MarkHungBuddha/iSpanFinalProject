package com.peko.houshoukaizokudan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class VerificationController {

    @GetMapping("/generate-verification-code")
    public String generateVerificationCode(HttpSession session) {
        // 生成验证码并将其存储在会话中
        String verificationCode = generateCode(6); // 生成一个 6 位的随机认证码
        session.setAttribute("verificationCode", verificationCode);
        return "verification-generated"; // 可以返回到一个确认页面
    }

    @GetMapping("/verify-verification-code")
    public String verifyVerificationCode(@RequestParam String userInputCode, HttpSession session) {
        // 获取存储在会话中的验证码
        String verificationCode = (String) session.getAttribute("verificationCode");

        if (verificationCode == null) {
            // 会话中没有验证码
            return "verification-failure"; // 可以返回到一个失败页面
        }

        // 检查用户输入的验证码是否与存储在会话中的验证码匹配
        if (verificationCode.equals(userInputCode)) {
            // 验证成功
            return "verification-success"; // 可以返回到一个成功页面
        } else {
            // 验证失败
            return "verification-failure"; // 可以返回到一个失败页面
        }
    }

    private String generateCode(int length) {
        // 在这里实现生成随机认证码的逻辑
        // 返回指定长度的随机字符串
        return "YourGeneratedCode"; // 此处替换为生成代码的实现
    }
}