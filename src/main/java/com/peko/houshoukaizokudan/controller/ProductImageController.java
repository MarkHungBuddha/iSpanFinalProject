package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peko.houshoukaizokudan.service.ProductImageService;

import io.jsonwebtoken.io.IOException;

// ...

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService; // 注入ProductImageService

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws java.io.IOException {
//        try {
//            // 调用ProductImageService的uploadImage方法上传图片
//            String imageUrl = productImageService.uploadImage(file);
//
//            // 如果上传成功，返回图片的URL
//            return ResponseEntity.ok(imageUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 处理上传失败的情况
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上傳失敗");
//        }
//    }
}

