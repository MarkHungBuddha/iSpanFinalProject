package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;


public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	//模糊搜尋產品名稱
	
	@Query("FROM ProductBasic WHERE productname LIKE %:productname%")
	Page<ProductBasic> findProductBasicByproductname(@Param("productname") String productname, Pageable Pageable);
	
	
	
	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.sellermemberid WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithSeller(@Param("id") Integer id);

	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.productImage JOIN FETCH p.qandA JOIN FETCH p.productReview WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithRelationships(@Param("id") Integer id);
	
	List<ProductBasic> findBySellermemberid(Member sellermemberid);


	
}



