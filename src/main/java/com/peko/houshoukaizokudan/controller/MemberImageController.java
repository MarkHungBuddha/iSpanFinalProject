package com.peko.houshoukaizokudan.controller;


import com.peko.houshoukaizokudan.service.MemberImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.ProductImageService;

import io.jsonwebtoken.io.IOException;
@RestController
public class MemberImageController {

    @Autowired
    private MemberImageService memberImageService;

    @PostMapping("/public/api/member/upload")
    public ResponseEntity<String> uploadMemberImage(@RequestPart("file") MultipartFile file,
                                                    @RequestParam("memberId") Integer memberId) {
        try {
            String imageCode = memberImageService.uploadImage(file, memberId);
            return ResponseEntity.ok("https://i.imgur.com/" + imageCode + ".jpeg");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("文件上传失败");
        }
    }
}
