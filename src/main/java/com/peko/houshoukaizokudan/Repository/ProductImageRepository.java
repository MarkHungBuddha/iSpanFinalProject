package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

	
	@Query("SELECT pic.imagepath FROM ProductImage pic " +
		       "WHERE pic.productid.id = :productid " +
		       "AND pic.id = (SELECT MIN(pic2.id) FROM ProductImage pic2 WHERE pic2.productid.id = :productid)")
	String findImagepathByProductid(@Param("productid") Integer productid);

	@Query("SELECT pi.imagepath FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
	String findByProductIdAndOrderId(@Param("id") Integer id, @Param("od") Integer od);

	@Modifying
	@Query("DELETE FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
	void deleteProductImage(@Param("id") Integer id, @Param("od") Integer od);
	
	@Modifying
	@Query("DELETE FROM ProductImage pi WHERE pi.productid.id = :id AND pi.orderID = :od")
	void deleteById(@Param("id") Integer id, @Param("od") Integer od);

}
