package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    // 创建购物车
    public ShoppingCart createShoppingCart() {
        return new ShoppingCart();
    }

    // 添加商品到购物车
    public void addProductToCart(ShoppingCart cart, ProductBasic product, int quantity) {
       cart.addProduct(product);
    }

    // 更新购物车中商品的数量
    public void updateProductQuantity(ShoppingCart cart,ProductBasic product, int quantity) {
        cart.updateProductQuantity(product, quantity);
    }

    // 从购物车中移除商品
    public void removeProductFromCart(ShoppingCart cart, ProductBasic product , int quantity) {
    }
}
   