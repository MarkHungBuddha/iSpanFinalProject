package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.DTO.WishlistDTO;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.Wishlist;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.WishListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    //買家新增商品進願望清單
    @PostMapping("/customer/api/wishlist/{productId}")
    public ResponseEntity<WishlistDTO> addProduct(HttpSession session, @PathVariable Integer productId) {
        try {
            Member member = (Member) session.getAttribute("loginUser");
            WishlistDTO addedProduct = wishListService.addProductToWishList(member.getId(), productId);
            if (addedProduct == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(addedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //買家從願望清單刪除商品
    @DeleteMapping("/customer/api/wishlist/{productId}")
    public ResponseEntity<WishlistDTO> removeProduct(HttpSession session, @PathVariable Integer productId) {
        Member member = (Member) session.getAttribute("loginUser");
        try {
            WishlistDTO removedProduct = wishListService.removeProductFromWishList(member.getId(), productId);
            return ResponseEntity.ok(removedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //買家查看願望清單商品
    @GetMapping("/public/api/wishlist")
    public ResponseEntity<List<WishlistDTO>> getWishList(HttpSession session) {
        try {
        Member member = (Member) session.getAttribute("loginUser");
            List<WishlistDTO> wishList = wishListService.getWishList(member.getId());
            return ResponseEntity.ok(wishList); //回傳商品列表
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
