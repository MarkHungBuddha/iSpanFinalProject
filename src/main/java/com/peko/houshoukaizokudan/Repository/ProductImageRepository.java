package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	
	@Query("SELECT pic.imagepath FROM ProductImage pic " +
		       "WHERE pic.productid.id = :productid " +
		       "AND pic.id = (SELECT MIN(pic2.id) FROM ProductImage pic2 WHERE pic2.productid.id = :productid)")
	String findImagepathByProductid(@Param("productid") Integer productid);

}
