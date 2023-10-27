package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.peko.houshoukaizokudan.Repository.MemberRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import com.peko.houshoukaizokudan.service.ProductBasicService;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductBasicService productBasicService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductBasic>> getAllProducts() {
        List<ProductBasic> products = productBasicService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/cart")
    public List<ShoppingCartDto> getShoppingCart(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
        	Integer memberId = loginUser.getId();
        	List<ShoppingCartDto> cartItems = shoppingCartService.GetCartItem(memberId);
            return cartItems;
        } 
        else {
        	return new ArrayList<>();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") Integer productId, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            Integer quantity = shoppingCartService.CheckQuantityByMember(loginUser.getId(), productId);
            ProductBasic product = productBasicService.findById(productId);
            if (quantity == product.getQuantity()) {
                return ResponseEntity.badRequest().body("庫存不足！");
            } else {
                shoppingCartService.addProductToCart(loginUser, product);
                return ResponseEntity.ok(loginUser.getId() + " 已新增 " + productId + " 商品");
            }
        } else {
            return ResponseEntity.badRequest().body("尚未登入");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("transactionId") Integer transactionId, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            shoppingCartService.ClearCartItem(loginUser, transactionId);
            return ResponseEntity.ok("商品已從購物車中移除");
        } else {
            return ResponseEntity.badRequest().body("尚未登入");
        }
    }


}