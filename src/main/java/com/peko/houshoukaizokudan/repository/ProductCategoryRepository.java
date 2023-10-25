package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{
	
	//搜尋類別id
	@Query(value = "from ProductBasic where categoryid.id = :categoryid")
	Page<ProductBasic> findProductBasicByCategoryId(@Param("categoryid") Integer categoryid, Pageable pageable);

}
