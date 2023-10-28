package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.Wishlist;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.WishListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WishlistController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductBasicService ProductBasicService;

    //新增商品
    @PostMapping("/addProduct/{productId}")
    public ResponseEntity<?> addProduct(HttpSession session, @PathVariable Integer productId) {
        Member member = (Member) session.getAttribute("member");

        try {
            wishListService.addProductToWishList(member.getId(), productId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }


    //刪除商品
    @DeleteMapping("/removeProduct/{productId}")
    public ResponseEntity<?> removeProduct(HttpSession session, @PathVariable Integer productId) {
        Member member = (Member) session.getAttribute("member");

        try {
            wishListService.removeProductFromWishList(member.getId(), productId);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }


    }

    //使用者id找商品
    @GetMapping("/getWishList")
    public ResponseEntity<?> getWishList(HttpSession session) {
        Member member = (Member) session.getAttribute("member");

        try {
            List<Integer> wishList = wishListService.getWishList(member.getId());
            return ResponseEntity.ok(wishList); //回傳商品列表
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
