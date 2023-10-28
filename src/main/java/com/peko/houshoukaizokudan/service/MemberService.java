package com.peko.houshoukaizokudan.service;

import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.Repository.MemberRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberService {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private final String CLIENT_ID = "Bearer 9394e99b2cdc13a531746679fe2ded9638bfbd91";  // 替換為你的 Imgur Client ID


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


    public String uploadImage(MultipartFile file) throws IOException, java.io.IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 检查文件是否存在
        if (file.isEmpty()) {
            throw new IllegalArgumentException("請上傳一個文件");
        }

        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Client-ID " + CLIENT_ID);

            // 将 MultipartFile 转换为字节数组
            byte[] fileContent = file.getBytes();

            // 创建请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

            // 从响应中获取图片网址
            String imageUrl = response.getBody();
            String pattern = "https://i.imgur.com/(\\w{7})\\.(?:jpeg|png)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(imageUrl);

            String extractedCode = "";
            if (m.find()) {
                extractedCode = m.group(1);
            }

            System.out.println(extractedCode);
            // 保存 ProductImage 到数据库
            System.out.println("Link:"+extractedCode);



            // 返回图片网址
            return imageUrl;
        } catch (IOException e) {
            // 处理错误
            throw new RuntimeException("文件上傳失敗");
        }
    }
    
    
}
