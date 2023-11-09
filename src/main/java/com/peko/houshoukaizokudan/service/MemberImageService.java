package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberImageService {

	 private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
	    private final String CLIENT_ID = "Bearer 70b29a551ba2e83d346bc604311ebf6c950c4542";

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public String uploadImage(MultipartFile file, Integer memberId) throws java.io.IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("请上传一个文件");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID " + CLIENT_ID);

        byte[] fileContent = file.getBytes();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

        String imageUrl = response.getBody();
        String imageCode = extractImageCode(imageUrl);

        updateMemberImagepath(memberId, imageCode);

        return imageCode;
    }

    private void updateMemberImagepath(Integer memberId, String imageCode) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("找不到ID为 " + memberId + " 的会员"));
        member.setMemberimgpath(imageCode);
        memberRepository.save(member);
    }

    private String extractImageCode(String imageUrl) {
        String pattern = "https://i.imgur.com/(\\w{7})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(imageUrl);

        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("无法从响应中提取图片代码");
        }
    }
}
