package com.peko.houshoukaizokudan.Repository;


import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {


    @Query("SELECT pb FROM ProductBasic pb JOIN pb.shoppingCart sc WHERE sc.memberid.id = :userId AND pb.id IN :productIds")
    List<ProductBasic> findProductsByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIds") List<Integer> productIds);







}
