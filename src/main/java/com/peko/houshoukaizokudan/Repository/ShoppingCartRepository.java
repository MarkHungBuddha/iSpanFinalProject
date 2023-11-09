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


    @Query(value = "SELECT quantity from ShoppingCart where productid=?1 and memberid=?2",nativeQuery = true)
    Integer findquantityByMemberid_IdAndProductid_Id(Integer productid, Integer memberid);


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


    @Query(value = "select SC from ShoppingCart SC left join fetch SC.productid PB where SC.memberid.id = ?1")
    List<ShoppingCart> getCartItemsByMemberId(Integer memberid);



    @Query(value = "select quantity from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer CheckCartItemQuantity(Integer transactionid);

    @Query(value = "select memberid from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer findmemberidbytransactionid(Integer transactionid);

    //transactionId找productid
    @Query(value = "select productid from ShoppingCart where transactionid = :transactionid",nativeQuery = true)
    Integer GetProductId(Integer transactionid);

//    @Query(value = "SELECT PB.quantity FROM ProductBasic PB WHERE PB.productid = ?2 PB.memberid=?1",nativeQuery = true)
//    Integer CheckQuantityByMember(Integer memberid,Integer productid);
//



    //用transactionid&memberid刪除購物車資料
    @Transactional
    @Modifying
    @Query(value = "Delete ShoppingCart where transactionid = ?1",nativeQuery = true)
    void ClearCartItem(Integer transactionid);




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


    boolean existsByMemberid_IdAndProductid_Id(Integer memberid,Integer Productid);



}