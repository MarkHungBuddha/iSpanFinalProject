package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    // 新增商品進購物車
    public void addProductToCart(ProductBasic product) {
        // 在這裡，您可以執行業務邏輯，例如檢查庫存、計算價格等

        // 創建購物車項目
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setProductid(product);
        cartItem.setQuantity(1); // 假設預設數量是1

        // 將購物車項目保存到資料庫
        shoppingCartRepository.save(cartItem);
    }

    // 更新購物車
    public void updateCart(ShoppingCart cartItem) {
        // 在這裡，您可以執行業務邏輯，例如檢查庫存、計算價格等

        // 更新購物車項目
        shoppingCartRepository.save(cartItem);
    }

    // 移除商品
    public void removeProductFromCart(Integer productId) {
        // 根據商品ID查找購物車項目並刪除
        shoppingCartRepository.deleteById(productId);
    }

    // 清空購物車
    public void clearCart() {
        // 刪除購物車中的所有項目
        shoppingCartRepository.deleteAll();
    }
}