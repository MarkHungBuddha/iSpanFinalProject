package com.peko.houshoukaizokudan.controller;


import com.peko.houshoukaizokudan.DTO.ProductQandADTO;
import com.peko.houshoukaizokudan.model.QandA;
import com.peko.houshoukaizokudan.service.QandAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QandAController {

    @Autowired
    private QandAService qandAService;

    //找出廠商id還沒回覆QandA bv Page

    //session抓memeberid 傳入productid 新增Q

    //廠商刪除問題

    //編輯QandA

    //列出QandA by 廠商id bypage

    //列出qanda by productid bypage
    @GetMapping("/product/qanda/{productid}")
    public ResponseEntity<List<ProductQandADTO>> findProductQandAsByProductid(@PathVariable Integer productid){


        try{
            List<ProductQandADTO> qandaList = qandAService.findProductQandAsByProductid(productid);
            return ResponseEntity.ok(qandaList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }
}
