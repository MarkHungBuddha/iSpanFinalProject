package com.peko.houshoukaizokudan.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	
	List<ProductBasic> findProductBasicDataByproductnameLike(String productname);

//	//抓最新的
//	public ProductBasic findFirstByOrderIdDesc();
}
