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
	
	//價格範圍搜尋 + 分類名稱搜尋  
	//以ProductCategory 表找 categoryname 取的 categoryid
    @Query("SELECT pc.id FROM ProductCategory pc WHERE pc.categoryname = :categoryname")
    Integer findCategoryIdByCategoryName(String categoryname);

    //以ProductBasic 表外來鍵 categoryid 找到 price 進行價格範圍的過濾功能
    @Query("SELECT pb FROM ProductBasic pb " +
	       "WHERE pb.categoryid.id = :categoryid " +
	       "AND pb.price >= :minPrice AND pb.price <= :maxPrice")
	Page<ProductBasic> findProductBasicsByCategoryIdAndPriceRange(Integer categoryid, Double minPrice, Double maxPrice, Pageable pageable);
   
    
//	//搜尋類別id 沒有加價格範圍的
//	@Query(value = "from ProductBasic where categoryid.id = :categoryid")
//	Page<ProductBasic> findProductBasicByCategoryId(@Param("categoryid") Integer categoryid, Pageable pageable);


}





