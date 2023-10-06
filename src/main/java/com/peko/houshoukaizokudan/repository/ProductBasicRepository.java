package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ProductBasic;

public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	//模糊搜尋產品名稱
	
	List<ProductBasic> findProductBasicDataByproductnameLike(String productname);

}



