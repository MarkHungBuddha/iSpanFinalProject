package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	//模糊搜尋產品名稱
	
	List<ProductBasic> findProductBasicDataByproductnameLike(String productname);

	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.sellermemberid WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithSeller(@Param("id") Integer id);


	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.productImage JOIN FETCH p.qandA JOIN FETCH p.productReview WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithRelationships(@Param("id") Integer id);





}



