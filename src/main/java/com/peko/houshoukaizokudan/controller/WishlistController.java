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

@Controller
public class WishlistController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductBasicService ProductBasicService;

    //新增商品
    public ResponseEntity<?> addProduct(HttpSession session, int productId) {
        Member member = (Member) session.getAttribute("member");
        ProductBasic productBasic= ProductBasicService.findById(productId);
        Wishlist addWishList = new Wishlist().builder().memberid(member).productid(productBasic).build();

        wishListService.addProductToWishList(addWishList);
        return ResponseEntity.ok(member);
    }



    //刪除商品
    public  ResponseEntity<?> removeProduct(HttpSession session, int productId) {
        Member member = (Member) session.getAttribute("member");
        wishListService.removeProductFromWishList(productId);
        return ResponseEntity.ok(member);

    }

    //使用者id找商品

}
