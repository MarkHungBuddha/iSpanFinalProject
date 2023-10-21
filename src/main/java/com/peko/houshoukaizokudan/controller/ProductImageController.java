package com.peko.houshoukaizokudan.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;

// ...

@RestController
public class ProductImageController {

    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";
    private final String CLIENT_ID = "Bearer 9394e99b2cdc13a531746679fe2ded9638bfbd91";  // 替換為你的 Imgur Client ID

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws java.io.IOException {
        RestTemplate restTemplate = new RestTemplate();

        // 檢查文件是否存在
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("請上傳一個文件");
        }

        try {
            // 設置請求標頭
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Client-ID " + CLIENT_ID);

            // 將 MultipartFile 轉換為字節數組
            byte[] fileContent = file.getBytes();

            // 建立請求體
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 發送請求
            ResponseEntity<String> response = restTemplate.postForEntity(IMGUR_UPLOAD_URL, requestEntity, String.class);

            // 從響應中獲取圖片網址
            // 你可能需要使用 JSON 解析工具，例如 Jackson，來解析響應並獲取圖片網址。
            // 這裡僅為示例，所以直接返回響應字符串。
            return ResponseEntity.ok(response.getBody());
        } catch (IOException e) {
            // 處理錯誤
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上傳失敗");
        }
    }
}
