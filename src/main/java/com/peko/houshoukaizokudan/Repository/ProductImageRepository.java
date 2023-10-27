package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductImage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
	
	//1個商品會有多張圖片, 取的imagepath 資料
	//外來鍵 productid 取得多筆重複的值,orderID 升序排序,取第一行會是最小orderID值
	@Query(value = "SELECT TOP 1 pic.imagepath FROM ProductImage pic " 
	        + "WHERE pic.productid = :productid " 
	        + "ORDER BY pic.orderID ASC", nativeQuery = true)
	String findImagepathByProductid(@Param("productid") Integer productid);

}
