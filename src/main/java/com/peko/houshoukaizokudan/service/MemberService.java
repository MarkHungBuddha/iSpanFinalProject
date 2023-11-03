package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.DTO.MemberDTO;

@Service
public class MemberService {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private final String CLIENT_ID = "Bearer 9394e99b2cdc13a531746679fe2ded9638bfbd91";  // 替換為你的 Imgur Client ID

    @Autowired
    private PasswordEncoder pwdEncoder;

    @Autowired
    private MemberRepository usersRepo;

    public Member addUser(Member user) {
        String encodedPwd = pwdEncoder.encode(user.getPasswdbcrypt()); // 加密
        user.setPasswdbcrypt(encodedPwd);
        return usersRepo.save(user);
    }

    public boolean checkIfUsernameExist(String username) {
        Member dbUser = usersRepo.findByUsername(username);
        return dbUser != null;
    }
    
    
    
    public boolean checkIfEmailExist(String email) {
        Member dbUser = usersRepo.findByEmail(email);
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

    public Member findById(Integer id) {
        return usersRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + id + " 的會員"));
    }

    public Member updateMember(MemberDTO memberDTO) {
        Member existingMember = usersRepo.findById(memberDTO.getId())
            .orElseThrow(() -> new RuntimeException("找不到 ID 為 " + memberDTO.getId() + " 的會員"));

        // 在这里更新成员的详细信息
        existingMember.setUsername(memberDTO.getUsername());
        existingMember.setFirstname(memberDTO.getFirstname());
        existingMember.setLastname(memberDTO.getLastname());
        existingMember.setCity(memberDTO.getCity());
        existingMember.setCountry(memberDTO.getCountry());
        existingMember.setGender(memberDTO.getGender());
        existingMember.setPhone(memberDTO.getPhone());
        existingMember.setPostalcode(memberDTO.getPostalcode());
        existingMember.setRegion(memberDTO.getRegion());
        existingMember.setStreet(memberDTO.getStreet());
        existingMember.setBirthdate(memberDTO.getBirthdate());
        // 根据需要设置其他字段

        return usersRepo.save(existingMember);
    }

    public void deleteMember(Integer memberId) {
        usersRepo.deleteById(memberId);
    }

    public Member findByUsername(String username) {
        return usersRepo.findByUsername(username);
    }

    public String uploadImage(MultipartFile file) throws IOException {
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
            System.out.println("Link:" + extractedCode);

            // 返回图片网址
            return imageUrl;
        } catch (IOException e) {
            // 处理错误
            throw new RuntimeException("文件上傳失敗");
        }
    }
    public MemberDTO findDTOByEmail(String email) {
        Member member = null; // 初始化为 null

        try {
            member = usersRepo.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace(); // 根据需要处理异常
        }

        if (member != null) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setId(member.getId());
            memberDTO.setUsername(member.getUsername());
            // 复制其他属性...
            return memberDTO;
        }

        return null;
    }

}
