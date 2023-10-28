package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductBasic;

public interface ProductBasicRepository  extends JpaRepository<ProductBasic,Integer> {
    List<ProductBasic> findByProductnameLike(String keyword);
    
    @Query(value = "SELECT quantity FROM ProductBasic WHERE productid = :productId", nativeQuery = true)
    Integer findQuantityById(@Param("productId") Integer c);

}