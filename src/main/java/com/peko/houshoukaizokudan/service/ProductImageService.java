package com.peko.houshoukaizokudan.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;

import io.jsonwebtoken.io.IOException;


@Service
public class ProductImageService {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private final String CLIENT_ID = "Bearer 9394e99b2cdc13a531746679fe2ded9638bfbd91";  // 替換為你的 Imgur Client ID

    @Autowired
    private ProductImageRepository productImageRepository;  // 假设你有一个ProductImageRepository用于与数据库交互

    @Autowired
    private ProductBasicRepository productBasicRepository;
    
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