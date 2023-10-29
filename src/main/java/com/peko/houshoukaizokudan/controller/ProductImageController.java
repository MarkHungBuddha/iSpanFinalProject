package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    //公開顯示商品圖片
    @GetMapping("/public/productImage/{productid}")
    public ResponseEntity<List<String>> productImage(@PathVariable Integer productid) {


        try {
            List<String> ProductPathList = productImageService.findImageByProductId(productid);

            return ResponseEntity.ok(ProductPathList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}

