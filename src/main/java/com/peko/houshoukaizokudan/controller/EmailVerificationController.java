package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.service.EmailVerificationService;

@RestController
@RequestMapping("/email")
public class EmailVerificationController {

    @Autowired
    private EmailVerificationService verificationService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        if (verificationService.isValidToken(token)) {
            return new ResponseEntity<>("邮箱验证成功！", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("无效的验证令牌", HttpStatus.BAD_REQUEST);
        }
    }
}