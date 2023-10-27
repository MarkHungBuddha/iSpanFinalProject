package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
	//1個商品會有多張圖片
	//外來鍵 productid 取得多筆重複的值，再用 MIN(orderID)取最小值
	@Query("SELECT pic.imagepath FROM ProductImage pic " +
		       "WHERE pic.productid.id = :productid " +
		       "AND pic.orderID = (SELECT MIN(pic2.orderID) FROM ProductImage pic2 WHERE pic2.productid.id = :productid)")
	String findImagepathByProductid(@Param("productid") Integer productid);
	
}
