package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/addProduct")
    public void addProductToCart(@RequestBody ProductBasic product, HttpSession session) {
        // 檢查Session中是否已經存在購物車
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        // 將商品添加到購物車
        shoppingCartService.addProductToCart(product);
    }
    // 更新購物車
    @PutMapping("/update")
    public void updateCart(@RequestBody ShoppingCart cartItem) {
        shoppingCartService.updateCart(cartItem);
    }

    // 移除商品
    @DeleteMapping("/removeProduct/{productId}")
    public void removeProductFromCart(@PathVariable Integer productId) {
        shoppingCartService.removeProductFromCart(productId);
    }

    // 清空購物車
    @DeleteMapping("/clear")
    public void clearCart() {
        shoppingCartService.clearCart();
    }
}