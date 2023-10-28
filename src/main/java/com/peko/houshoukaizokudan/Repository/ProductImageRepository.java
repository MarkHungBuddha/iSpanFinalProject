package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
	@Query(value = "SELECT TOP 1 pic.imagepath FROM ProductImage pic " 
	         + "WHERE pic.productid = :productid " 
	         + "ORDER BY pic.orderID ASC", nativeQuery = true)
	 String findImagepathByProductid(@Param("productid") Integer productid);
}
