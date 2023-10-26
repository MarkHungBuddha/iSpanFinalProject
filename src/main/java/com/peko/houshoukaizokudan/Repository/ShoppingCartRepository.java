package com.peko.houshoukaizokudan.Repository;


import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {


    @Query(value = "SELECT pb.* " +
            "FROM dbo.ProductBasic pb " +
            "INNER JOIN dbo.ShoppingCart sc ON pb.productid = sc.productid " +
            "WHERE sc.memberid = :userId " +
            "AND pb.productid IN :productIds", nativeQuery = true)
    List<ProductBasic> findProductsByUserIdAndProductIds(@Param("userId") Integer userId, @Param("productIds") List<Integer> productIds);


}
