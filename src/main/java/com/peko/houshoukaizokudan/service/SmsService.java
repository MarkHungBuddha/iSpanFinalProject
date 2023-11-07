package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.peko.houshoukaizokudan.DTO.MemberDTO;

import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

@Service
public class SmsService {

    @Value("${sms.username}")
    private String username;

    @Value("${sms.password}")
    private String password;

    @Value("${sms.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    private MemberService memberService;

    
    public SmsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 发送验证码到手机
    public void sendPhoneVCode(String mobile) {
        // 查找对应的用户
        MemberDTO userDTO = memberService.findDTOByPhone(mobile);
        if (userDTO != null) {
            // 生成验证码
            String verificationCode = VerificationCodeGenerator.generateCode(6);

            // 将验证码存储为 resetToken
            userDTO.setResetToken(verificationCode);
            memberService.updateMember(userDTO);

            // 构建短信内容
            String message = "您的認證碼為: " + verificationCode;

            // 使用 sendSms 方法发送短信
            sendSms(mobile, message);
        }
    }

    // 验证接收到的验证码
    public boolean verifyVerificationCode(String mobile, String verificationCode) {
        MemberDTO userDTO = memberService.findDTOByPhone(mobile);
        if (userDTO != null && verificationCode.equals(userDTO.getResetToken())) {
            // 验证成功，更新用户类型并清除resetToken
            userDTO.setMembertypeid(2); // 假设2代表验证通过的用户类型
            userDTO.setResetToken(null); // 清除验证码
            memberService.updateMember(userDTO);
            return true;
        } else {
            // 验证失败，清除resetToken
            if (userDTO != null) {
                userDTO.setResetToken(null);
                memberService.updateMember(userDTO);
            }
            return false;
        }
    }

    // 发送短信的方法
    public void sendSms(String mobile, String message) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("username", username)
                    .queryParam("password", password)
                    .queryParam("mobile", mobile)
                    .queryParam("message", message)
                    .toUriString();

            String response = restTemplate.postForObject(url, null, String.class);
            System.out.println("Response from SMS API: " + response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}