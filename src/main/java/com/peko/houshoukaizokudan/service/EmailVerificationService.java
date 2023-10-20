package com.peko.houshoukaizokudan.service;

import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    public boolean isValidToken(String token) {
        // 实现验证逻辑，例如检查数据库中是否存在令牌并标记验证通过
        // 如果验证通过，返回 true；否则返回 false
        return true; // 或者 false
    }
}