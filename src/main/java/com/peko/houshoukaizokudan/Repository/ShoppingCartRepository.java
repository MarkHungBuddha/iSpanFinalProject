package com.peko.houshoukaizokudan.Repository;
import com.peko.houshoukaizokudan.model.ShoppingCart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    @Query(value = "select IsNull((SELECT quantity FROM shoppingcart WHERE memberid = ?1 and productid = ?2),0)", nativeQuery = true)
    Integer CheckProductByMemberId(Integer memberid,Integer productid);
    
    @Transactional
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = ?3 WHERE memberid = ?1 and productid = ?2", nativeQuery = true)
    void UpdateCartQuantity(Integer memberid,Integer productid,Integer quantity);
    
    
    @Query(value = "select SC.transactionid,SC.productid,SC.memberid,PB.productname,SC.quantity,PB.price from ShoppingCart SC left join ProductBasic PB on SC.productid = PB.productid where SC.memberid = ?1",nativeQuery = true)
    List<ShoppingCart> GetCartItem(Integer memberid);
    
    @Query(value = "select quantity from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer CheckCartItem(Integer transactionid);

    @Query(value = "select productid from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer GetProductId(Integer transactionid);
   
    @Query(value = "select SC.quantity from ShoppingCart SC left join ProductBasic PB on SC.productid = PB.productid where SC.memberid = ?1 and PB.productid = ?2",nativeQuery = true)
    Integer CheckQuantityByMember(Integer memberid,Integer productid);
   
    
    @Transactional
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = quantity - 1 where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void MinusCartItem(Integer memberid,Integer transactionid);
    
    @Transactional
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = quantity + 1 where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void PlusCartItem(Integer memberid,Integer transactionid);
    
    @Transactional
    @Modifying
    @Query(value = "Delete ShoppingCart where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void ClearCartItem(Integer memberid,Integer transactionid);
}


