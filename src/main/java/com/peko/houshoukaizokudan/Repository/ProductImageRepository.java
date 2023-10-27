package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peko.houshoukaizokudan.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	// 產品id 找 圖片7碼 (第一張)
	@Query(value = "SELECT TOP 1 imagepath FROM ProductImage  WHERE productid = ?1 ORDER BY orderID ASC", nativeQuery = true)
	String findProductImagebyproductid(Integer productid);

}
