package com.peko.houshoukaizokudan.Repository;


import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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


    @Query(value = "select SC.transactionid,SC.productid,SC.memberid,PB.productname,SC.quantity,PB.price from ShoppingCart SC left join ProductBasic PB on SC.productid = PB.productid where SC.memberid = ?1",nativeQuery = true)
    List<Object[]> GetCartItem(Integer memberid);

    @Query(value = "select quantity from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer CheckCartItem(Integer transactionid);


    //transactionId找productid
    @Query(value = "select productid from ShoppingCart where transactionid = :transactionid",nativeQuery = true)
    Integer GetProductId(Integer transactionid);

//    @Query(value = "SELECT PB.quantity FROM ProductBasic PB WHERE PB.productid = ?2 PB.memberid=?1",nativeQuery = true)
//    Integer CheckQuantityByMember(Integer memberid,Integer productid);
//

    @Transactional
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = quantity - 1 where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void MinusCartItem(Integer memberid,Integer transactionid);

    @Transactional
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = quantity + 1 where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void PlusCartItem(Integer memberid,Integer transactionid);


    //用transactionid&memberid刪除購物車資料
    @Transactional
    @Modifying
    @Query(value = "Delete ShoppingCart where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void ClearCartItem(Integer memberid,Integer transactionid);


    //productid&memberid刪除購物車資料
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ShoppingCart WHERE memberid = ?1 AND productid = ?2", nativeQuery = true)
    void ClearCartItembyProductId(Integer memberid, Integer productid);


    @Query(value = "select * from ShoppingCart where memberid = ?1 and productid = ?2",nativeQuery = true)
    ShoppingCart findByIdAndUser(Integer memberid, Integer productid);

    @Transactional
    @Modifying
    @Query("UPDATE ShoppingCart s SET s.quantity = :newQuantity WHERE s.memberid.id = :memberId AND s.productid.id = :productId")
    void updateQuantityByMemberIdAndProductId(Integer memberId, Integer productId, Integer newQuantity);




}