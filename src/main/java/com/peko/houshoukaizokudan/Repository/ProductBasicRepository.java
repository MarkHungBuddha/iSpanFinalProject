package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

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

	//找商品賣家id
	@Query(value ="SELECT sellermemberid FROM ProductBasic WHERE productid = ?1", nativeQuery = true)
	Integer findProductBasicSellerIdByproductId(Integer productID);

	// 找商品 by 商品id
	ProductBasic findProductById(int productId);

	// 找商品庫存 by 商品id
	@Query(value ="SELECT quantity FROM ProductBasic WHERE productid = ?1", nativeQuery = true)
	Integer findProductByProductid(Integer productid);

	// 更新庫存數量
	@Modifying
	@Query(value = "UPDATE ProductBasic SET quantity = ?2 WHERE productid = ?1", nativeQuery = true)
	void updateProductQuantity(Integer productid, int stockQuantity);





}



