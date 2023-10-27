package com.peko.houshoukaizokudan.controller;


import com.peko.houshoukaizokudan.DTO.ProductReviewDTO;
import com.peko.houshoukaizokudan.model.ProductReview;
import com.peko.houshoukaizokudan.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;

    //新增Review(要跟orderdetail有關)
//      1.api 傳入 orderid orderdetailid productid memberid
//      2.檢查這筆訂單是不是這個會員買的(orderid,memberid)
//      3.檢查這筆訂單有沒有買這個商品(orderdetailid,productid)
//      4.建立新的productreview

    //product review by productid by page(平均評價)

    //product review by time by page
    @GetMapping("/productreview/{productid}")
    public ResponseEntity<List<ProductReviewDTO>> findProductReviewByProductid(@PathVariable Integer productid){
        try{
            List<ProductReviewDTO> productReviews=productReviewService.findProductReviewByProductid(productid);
            return ResponseEntity.ok(productReviews);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }




}
