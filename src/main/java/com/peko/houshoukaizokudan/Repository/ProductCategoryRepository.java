package com.peko.houshoukaizokudan.Repository;

import java.util.List;

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

	//價格範圍搜尋    
 	    @Query("SELECT pc.id FROM ProductCategory pc WHERE pc.categoryname = :categoryname")
	    Integer findCategoryIdByCategoryName(String categoryname);

	    
	    @Query("SELECT pb FROM ProductBasic pb " +
	    	       "WHERE pb.categoryid.id = :categoryid " +
	    	       "AND pb.price >= :minPrice AND pb.price <= :maxPrice")
	    	Page<ProductBasic> findProductBasicsByCategoryIdAndPriceRange(Integer categoryid, Double minPrice, Double maxPrice, Pageable pageable);
	    }





