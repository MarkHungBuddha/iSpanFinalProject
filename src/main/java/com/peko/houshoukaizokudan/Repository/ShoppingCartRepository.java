package com.peko.houshoukaizokudan.Repository;


import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
}
