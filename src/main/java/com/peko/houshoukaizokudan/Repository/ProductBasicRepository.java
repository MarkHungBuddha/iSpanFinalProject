package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import java.util.Optional;

import com.peko.houshoukaizokudan.model.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.peko.houshoukaizokudan.model.ProductBasic;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer> {
	
	//模糊搜尋產品名稱

	List<ProductBasic> findProductBasicDataByproductnameLike(String productname);

	@Query("SELECT p FROM ProductBasic p JOIN FETCH p.sellermemberid WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithSeller(@Param("id") Integer id);


	@Query("SELECT p FROM ProductBasic p WHERE p.id = :id")
	Optional<ProductBasic> findByIdWithRelationships(@Param("id") Integer id);



	//模糊搜尋產品名稱

	@Query("FROM ProductBasic WHERE productname LIKE %:productname%")
	Page<ProductBasic> findProductBasicByproductname(@Param("productname") String productname, Pageable Pageable);


	// 模糊搜尋 + 價格範圍
	//ProductBasic 表 模糊搜尋 productname 存入產品名稱+頁碼+價格範圍
	@Query("FROM ProductBasic WHERE productname LIKE %:productname% AND (price >= :minPrice AND price <= :maxPrice)")
	Page<ProductBasic> findProductBasicByProductNameAndPriceRange(@Param("productname") String productname, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);




	List<ProductBasic> findBySellermemberid(Member sellermemberid);

	@Query("SELECT pb FROM ProductBasic pb WHERE pb.productname = :productname")
	Page<ProductBasic> findProductBasicByProductname(@Param("productname") String productname, Pageable pageable);

	//    @Query("SELECT pb FROM ProductBasic pb WHERE pb.sellermemberid = :memberId")
	@Query(value = "SELECT * FROM ProductBasic pb WHERE pb.sellermemberid = :memberId", nativeQuery = true)
	Page<ProductBasic> findProductBasicBySellermemberid(@Param("memberId") Integer memberId, Pageable pageable);



	@Query(value = "SELECT * FROM productbasic pb WHERE pb.sellermemberid = :memberIdd AND pb.productname LIKE %:productname%",
			nativeQuery = true)
	Page<ProductBasic> findProductBasicBySellermemberidAndProductnameContaining(Integer memberIdd, String productname,
																				Pageable pageable);



	@Query("SELECT pb FROM ProductBasic pb WHERE pb.id = :id AND pb.sellermemberid.id = :memberIdd")
	Optional<ProductBasic> findByIdAndSellerId(@Param("id") Integer id, @Param("memberIdd") Integer memberIdd);



	@Query(value = "SELECT quantity FROM ProductBasic WHERE productid = :productId", nativeQuery = true)
	Integer findQuantityById(@Param("productId") Integer c);

	// 找商品 by 商品id
	ProductBasic findProductById(int productId);

	// 找商品庫存 by 商品id
	@Query(value ="SELECT quantity FROM ProductBasic WHERE productid = ?1", nativeQuery = true)
	Integer findProductByProductid(Integer productid);


	//找商品賣家id
	@Query(value ="SELECT sellermemberid FROM ProductBasic WHERE productid = ?1", nativeQuery = true)
	Integer findProductBasicSellerIdByproductId(Integer productID);


	// 更新庫存數量
	@Modifying
	@Query(value = "UPDATE ProductBasic SET quantity = ?2 WHERE productid = ?1", nativeQuery = true)
	void updateProductQuantity(Integer productid, int stockQuantity);
}



