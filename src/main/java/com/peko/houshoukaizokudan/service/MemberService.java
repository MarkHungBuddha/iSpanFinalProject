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

import jakarta.transaction.Transactional;

import com.peko.houshoukaizokudan.DTO.MemberDTO;
@Transactional
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
        // 根據用戶名查找用戶
    	
        Member dbUser = usersRepo.findByUsername(username);
        
        // 如果用戶存在，且密碼匹配，則返回用戶信息
        if (dbUser != null && pwdEncoder.matches(inputPwd, dbUser.getPasswdbcrypt())) {
            return dbUser;
        }

        // 如果用戶不存在或密碼不匹配，返回 null
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
        existingMember.setResetToken(memberDTO.getResetToken());
        if (memberDTO.getPasswdbcrypt() != null && !memberDTO.getPasswdbcrypt().isEmpty()) {
            // 不再进行加密，直接设置密码
            existingMember.setPasswdbcrypt(memberDTO.getPasswdbcrypt());
        }
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
    public MemberDTO findDTOByUsername(String username) {
        Member member = usersRepo.findByUsername(username);
        if (member != null) {
            return convertToDTO(member);
        } else {
            System.out.println("User not found with username: " + username);
            return null;
        }
    }
    public MemberDTO findDTOByEmail(String email) {
        Member member = usersRepo.findByEmail(email);
        System.out.println(email+"這邊");
        if (member != null) {
            System.out.println(convertToDTO(member));
            return convertToDTO(member);
        }
        System.out.println("沒料");
        return null;
    }
    private MemberDTO convertToDTO(Member member) {
        if (member == null) {
            return null;
        }
        MemberDTO userDTO = new MemberDTO();
        userDTO.setId(member.getId());
        userDTO.setMemberimgpath(member.getMemberimgpath());
        userDTO.setUsername(member.getUsername());
        userDTO.setFirstname(member.getFirstname());
        userDTO.setLastname(member.getLastname());
        userDTO.setGender(member.getGender());
        userDTO.setBirthdate(member.getBirthdate());
        userDTO.setPhone(member.getPhone());
        userDTO.setEmail(member.getEmail());
        userDTO.setMembercreationdate(member.getMembercreationdate());
        userDTO.setCountry(member.getCountry());
        userDTO.setCity(member.getCity());
        userDTO.setRegion(member.getRegion());
        userDTO.setStreet(member.getStreet());
        userDTO.setPostalcode(member.getPostalcode());
        userDTO.setMembertypeid(member.getMembertypeid().getId());
        userDTO.setMembertypename(member.getMembertypeid().getMembertypename());
        userDTO.setMemberTypeDescription(member.getMembertypeid().getMemberTypeDescription());
        userDTO.setPasswdbcrypt(member.getPasswdbcrypt());
        userDTO.setResetToken(member.getResetToken());
        return userDTO;
    }
    public String uploadImage(MultipartFile file, Integer memberId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please upload a file.");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID " + CLIENT_ID);

        byte[] fileContent = file.getBytes();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();  // Or you might want to generate a new filename
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

        String imageUrl = extractImageUrl(response.getBody());

        Member member = usersRepo.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        member.setMemberimgpath(imageUrl);  // Assume 'member' has a field 'memberimgpath' to store the image URL
        usersRepo.save(member);

        return imageUrl;
    }

    private String extractImageUrl(String responseBody) {
        String pattern = "https://i.imgur.com/(\\w+).(jpg|png|gif)";  // Adjust regex according to the expected URL format
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(responseBody);

        if (m.find()) {
            return m.group(0);
        } else {
            throw new RuntimeException("Failed to extract image URL from response.");
        }
    }
}
