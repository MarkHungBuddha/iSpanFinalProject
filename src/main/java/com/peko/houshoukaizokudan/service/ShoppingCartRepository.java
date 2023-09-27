package com.peko.houshoukaizokudan.service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peko.houshoukaizokudan.model.ShoppingCart;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    // 您可以在需要的情況下添加自定義的查詢方法
}