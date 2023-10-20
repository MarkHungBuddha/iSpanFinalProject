package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	
	
	@Query("SELECT pic.imagepath FROM ProductImage pic " +
		       "WHERE pic.productid.id = :productid " +
		       "AND pic.id = (SELECT MIN(pic2.id) FROM ProductImage pic2 WHERE pic2.productid.id = :productid)")
	String findImagepathByProductid(@Param("productid") Integer productid);

}
