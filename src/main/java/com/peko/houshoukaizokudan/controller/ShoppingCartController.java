package com.peko.houshoukaizokudan.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ShoppingCartService;
import javax.servlet.http.HttpSession;

@Controller
//@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
    
 // 显示购物车
    @GetMapping("/shoppingcart/view")
    public String viewCart() {
        // 从Session中获取购物车对象
//        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        // 将购物车对象添加到模型中，以便在视图中渲染
//        model.addAttribute("cart", cart);

        return "shoppingcart/shoppingCart"; // 对应购物车视图的HTML模板
    }

    @PostMapping("/ShoppingCart/addProduct")
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
    @PutMapping("/ShoppingCart/update")
    public void updateCart(@RequestBody ShoppingCart cartItem) {
        shoppingCartService.updateCart(cartItem);
    }

    // 移除商品
    @DeleteMapping("/ShoppingCart/removeProduct/{productId}")
    public void removeProductFromCart(@PathVariable Integer productId) {
        shoppingCartService.removeProductFromCart(productId);
    }

    // 清空購物車
    @DeleteMapping("/ShoppingCart/clear")
    public void clearCart() {
        shoppingCartService.clearCart();
    }
    
}