package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    private ProductBasicService productBasicService;


//    @PostMapping("/customer/api/addProduct")
//    public void addProductToCart(@RequestBody ProductBasic product, HttpSession session) {
//        // 檢查Session中是否已經存在購物車
//        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ShoppingCart();
//            session.setAttribute("cart", cart);
//        }
//
//        // 將商品添加到購物車
//        shoppingCartService.addProductToCart(product);
//    }
//    // 更新購物車
//    @PutMapping("/update")
//    public void updateCart(@RequestBody ShoppingCart cartItem) {
//        shoppingCartService.updateCart(cartItem);
//    }
//
//    // 移除商品
//    @DeleteMapping("/removeProduct/{productId}")
//    public void removeProductFromCart(@PathVariable Integer productId) {
//        shoppingCartService.removeProductFromCart(productId);
//    }

//    // 清空購物車
//    @DeleteMapping("/clear")
//    public void clearCart() {
//        shoppingCartService.clearCart();
//    }

//    @GetMapping("/products")
//    public ResponseEntity<List<ProductBasic>> getAllProducts() {
//        List<ProductBasic> products = productBasicService.listAllProducts();
//        return ResponseEntity.ok(products);
//    }

    //買家查看購物車
    @GetMapping("/customer/api/shoppingCart")
    public List<ShoppingCartDto> getShoppingCart(HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            Integer memberId = loginUser.getId();
            List<ShoppingCartDto> cartItems = shoppingCartService.getCartItemsByMemberId(memberId);
            return cartItems;
        } else {
            return new ArrayList<>();
        }
    }


    //買家新增商品到購物車
    @PostMapping("/customer/api/shoppingCart")
    public ResponseEntity<String> addProductToCart(@RequestParam("productId") Integer productId, HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        try {
            if (loginUser == null) {
                return ResponseEntity.badRequest().body("尚未登入");
            }
            if (productBasicService.findById(productId).getQuantity() < 1) {
                return ResponseEntity.badRequest().body("庫存不足！");
            }

            shoppingCartService.addProductToCart(
                    loginUser.getId(),
                    productBasicService.findById(productId).getId());
            return ResponseEntity.ok(loginUser.getId() + " 已新增 " + productId + " 商品");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);

        }

}

    //買家移除商品
    @DeleteMapping("/customer/api/shoppingCart")
    public ResponseEntity<String> removeProductFromCart(@RequestParam("transactionId") Integer transactionId,
                                                        HttpSession session) {
        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser != null) {
            shoppingCartService.ClearCartItem(loginUser, transactionId);
            return ResponseEntity.ok("商品已從購物車中移除");
        } else {
            return ResponseEntity.badRequest().body("尚未登入");
        }
    }


    //買家更改商品數量
    @PutMapping("/customer/api/change")
    public ResponseEntity<ShoppingCartDto> changeQuantity(
            @RequestParam("quantity") Integer quantity,
            @RequestParam("productid") Integer productid,
            HttpSession session)
            throws Exception {
        Member loginUser = (Member) session.getAttribute("loginUser");

        System.out.println("transactionId:" + productid);
        System.out.println("quantity" + quantity);
        if (loginUser != null) {
            if (quantity == 0) {
                shoppingCartService.ClearCartItembyProductId(loginUser, productid);
            }
            int memberid = loginUser.getId();
            System.out.println("loginUser.getId" + memberid);

            ShoppingCartDto updatedCart = shoppingCartService.changeQuantity(memberid, productid,
                    quantity);
            return ResponseEntity.ok(updatedCart);
        }
        return ResponseEntity.badRequest().build();
    }

}

