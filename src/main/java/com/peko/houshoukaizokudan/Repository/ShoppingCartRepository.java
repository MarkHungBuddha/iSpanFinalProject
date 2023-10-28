package com.peko.houshoukaizokudan.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

	@Query("SELECT pb FROM ProductBasic pb JOIN pb.shoppingCart sc WHERE sc.memberid.id = :userId AND pb.id IN :productIds")
	List<ProductBasic> findProductsByUserIdAndProductIds(
			@Param("userId") Integer userId,
			@Param("productIds") List<Integer> productIds);

	// 如果購買後商品數量為0 清除購物車內的商品
	@Modifying
	@Query(value ="DELETE FROM ShoppingCart WHERE productid = ?1 AND memberid = ?2", nativeQuery = true)
	void removeProductFromShoppingCart( Integer productid, Integer memberid);

	// 更新購物車商品數量
	@Modifying
	@Query(value ="UPDATE ShoppingCart SET quantity = ?3 WHERE productid = ?1 AND memberid = ?2", nativeQuery = true)
	void saveProductFromShoppingCart(Integer productid, Integer memberid, int carQuantity);

}
