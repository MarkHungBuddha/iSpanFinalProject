package com.peko.houshoukaizokudan.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.peko.houshoukaizokudan.DTO.MemberDTO;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.ProductImageService;

import io.jsonwebtoken.io.IOException;
@RestController
public class MemberImageController {

    @Autowired
    private ProductImageService productImageService; // 注入ProductImageService

    @Autowired
    private MemberService memberService; // 注入MemberService

    @PostMapping("/public/api/upload")
    public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file, @RequestPart("memberDTO") MemberDTO memberDTO) throws java.io.IOException {
        try {
            // 调用MemberService的uploadImage方法上传图片
            String imageUrl = memberService.uploadImage(file);

            // 如果上传成功，返回图片的URL

            // 更新用户的memberimgpath字段
            memberDTO.setMemberimgpath(imageUrl);
            memberService.updateMember(memberDTO);

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理上传失败的情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上傳失敗");
        }
    }
}
