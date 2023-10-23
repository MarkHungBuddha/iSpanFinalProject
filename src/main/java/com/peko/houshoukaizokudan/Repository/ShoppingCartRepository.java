package com.peko.houshoukaizokudan.Repository;
import com.peko.houshoukaizokudan.model.ShoppingCart;
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
}


