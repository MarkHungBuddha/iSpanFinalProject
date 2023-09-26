package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    private final ProductBasicService productBasicService;

    @Autowired
    public ShoppingCartController(ProductBasicService productBasicService) {
        this.productBasicService = productBasicService;
    }

    @GetMapping("/add-to-cart")
    public String addToCart(HttpSession session) {
        // 从ProductBasicService中获取商品数据（示例中假设您有一个获取商品的方法）
        List<ProductBasic> products = productBasicService.findProductBasicDataByProductName("Example Product");

        if (!products.isEmpty()) {
            // 获取第一个商品作为示例
            ProductBasic product = products.get(0);

            // 获取或创建购物车对象
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) {
                cart = new ShoppingCart();
            }

            // 将商品添加到购物车
            cart.addProduct(product);

            // 将更新后的购物车对象存入会话
            session.setAttribute("cart", cart);
        }

        // 重定向到购物车页面或其他适当的页面
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        // 从会话中获取购物车对象
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        // 将购物车对象传递给视图以供显示
        model.addAttribute("cart", cart);

        // 返回购物车页面的视图名
        return "cart"; // 这里假设有一个名为 "cart.html" 的视图模板用于显示购物车内容
    }
}
