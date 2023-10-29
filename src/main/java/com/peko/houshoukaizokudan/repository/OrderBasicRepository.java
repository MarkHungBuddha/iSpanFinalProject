package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import com.peko.houshoukaizokudan.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {


    boolean existsByIdAndBuyer_Id(Integer orderId, Integer memberId);

    @Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE SUBSTRING(ob.merchanttradedate, 1, 4) = :year AND ob.seller.id = :memberIdd")
    Integer findTotalAmountByYearAndSeller(@Param("year") Integer year, @Param("memberIdd") Integer memberIdd);
    @Query("SELECT SUM(ob.totalamount) FROM OrderBasic ob WHERE SUBSTRING(ob.merchanttradedate, 1, 4) = :year AND SUBSTRING(ob.merchanttradedate, 6, 2) = :month AND ob.seller.id = :memberIdd")
    Integer findTotalAmountByYearAndMonthAndSeller(@Param("year") Integer year, @Param("month") Integer month, @Param("memberIdd") Integer memberIdd);

    //買家找訂單 By 購買人(List)
    List<OrderBasic> findOrderBasicBybuyer(Member buyer);

    //買家找訂單 By 買家id(Page)
    Page<OrderBasic> findOrderBasicBybuyer(Member buyer, Pageable Pageable);

    //賣家找訂單 By 賣家id(Page)
    Page<OrderBasic> findOrderBasicByseller(Member seller, Pageable Pageable);

    //買家找訂單 By 訂單狀態
//	@Query(value = "SELECT * FROM OrderBasic OB INNER JOIN OrderStatus OS ON OB.statusID = OS.statusid WHERE OB.memberid = ?1 AND OS.statusID = ?2", nativeQuery = true)
    @Query(value = "SELECT * FROM OrderBasic OB WHERE OB.memberid = ?1 AND OB.statusid = ?2", nativeQuery = true)
    Page<OrderBasic> findOrderBasicByStatus( Integer memberid,  Integer statusid, Pageable pageable);

    //買家找訂單 By 訂單ID
    OrderBasic findOrderBasicById(int orderid);
    @Query(value = "select * from ShoppingCart where memberid = ?1 and productid = ?2",nativeQuery = true)
    ShoppingCart findByIdAndUser(Integer c, Integer productid);

    // 更新購物車商品數量
    @Modifying
    @Query(value ="UPDATE ShoppingCart SET quantity = ?3 WHERE productid = ?1 AND memberid = ?2", nativeQuery = true)
    void saveProductFromShoppingCart(Integer productid, Integer memberid, int carQuantity);


}
