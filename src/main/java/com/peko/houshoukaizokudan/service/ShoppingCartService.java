package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private  ShoppingCartRepository shoppingCartRepository;

    
    
     // 新增商品進購物車
    public void addProductToCart(Member member,ProductBasic product) {
    	Integer checkcart = shoppingCartRepository.CheckProductByMemberId(member.getId(),product.getId());
    	if (checkcart > 0) {
    		shoppingCartRepository.UpdateCartQuantity(member.getId(),product.getId(),checkcart + 1);
    	}
    	else {
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setMemberid(member);
        cartItem.setProductid(product);
        cartItem.setQuantity(1);
        shoppingCartRepository.save(cartItem);
    	}
    }

    // 更新購物車
    public void updateCart(ShoppingCart cartItem) {

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