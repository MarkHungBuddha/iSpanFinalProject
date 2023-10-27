package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {


    @Query("SELECT pi.imagepath FROM ProductImage pi WHERE pi.productid.id = :productId ORDER BY pi.orderID")
    List<String> findImagePathsByProductIdOrderByOrderID(Integer productId);
}
