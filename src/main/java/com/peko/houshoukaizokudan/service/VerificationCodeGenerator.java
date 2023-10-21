package com.peko.houshoukaizokudan.service;
import java.security.SecureRandom;

public class VerificationCodeGenerator {
	
    public static String generateCode(int length) {
        String allowedChars = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            code.append(allowedChars.charAt(index));
        }
        return code.toString();
    }
}