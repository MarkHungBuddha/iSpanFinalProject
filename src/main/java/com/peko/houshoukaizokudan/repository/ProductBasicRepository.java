package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {

	// 模糊搜尋產品名稱  **此功能目前已不使用  看其他人有沒有用到**
	List<ProductBasic> findProductBasicDataByproductnameLike(String productname);

//	// 模糊搜尋產品名稱    ** 此功能目前已不使用 **
//	//ProductBasic 表搜模糊尋 productname 存入產品名稱+頁碼
//	@Query("FROM ProductBasic WHERE productname LIKE %:productname%")
//	Page<ProductBasic> findProductBasicByproductname(@Param("productname") String productname, Pageable Pageable);
//	
	// 模糊搜尋 + 價格範圍
	//ProductBasic 表 模糊搜尋 productname 存入產品名稱+頁碼+價格範圍
	@Query("FROM ProductBasic WHERE productname LIKE %:productname% AND (price >= :minPrice AND price <= :maxPrice)")
	Page<ProductBasic> findProductBasicByProductNameAndPriceRange(@Param("productname") String productname, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);

	
	
	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.sellermemberid WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithSeller(@Param("id") Integer id);

	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.productImage JOIN FETCH p.qandA JOIN FETCH p.productReview WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithRelationships(@Param("id") Integer id);

}
