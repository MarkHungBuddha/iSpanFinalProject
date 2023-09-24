package com.peko.houshoukaizokudan.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	//模糊搜尋產品名稱
	
	List<ProductBasic> findProductBasicDataByproductnameLike(String name);

}
