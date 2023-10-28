package com.peko.houshoukaizokudan.Repository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ShoppingCart;

import java.util.List;

import com.peko.houshoukaizokudan.DTO.ShoppingCartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

    @Query(value = "select SC.transactionid,SC.productid,SC.memberid,PB.productname,SC.quantity,PB.price from ShoppingCart SC left join ProductBasic PB on SC.productid = PB.productid where SC.memberid = ?1",nativeQuery = true)
    List<Object[]> GetCartItem(Integer memberid);
    
    @Query(value = "select quantity from ShoppingCart where transactionid = ?1",nativeQuery = true)
    Integer CheckCartItem(Integer transactionid);

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
    
    @Transactional
    @Modifying
    @Query(value = "Delete ShoppingCart where memberid = ?1 and transactionid = ?2",nativeQuery = true)
    void ClearCartItem(Integer memberid,Integer transactionid);

    @Query(value = "select quantity from ShoppingCart where memberid = ?1 and transactionid = ?2",nativeQuery = true)
	ShoppingCart findByIdAndUser(Integer c, Integer transactionId);
    
    
    @Modifying
    @Query(value = "Update ShoppingCart set quantity = :quantity where transactionid = :transactionId 	",nativeQuery = true)
	void update(@Param("quantity") Integer quantity,@Param("transactionId") Integer transactionId);
     


    
}


