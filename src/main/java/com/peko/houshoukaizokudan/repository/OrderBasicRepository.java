package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import com.peko.houshoukaizokudan.DTO.RevenueDto;
import com.peko.houshoukaizokudan.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {


    boolean existsByIdAndBuyer_Id(Integer orderId, Integer memberId);

//    @Query(value = "SELECT * FROM dbo.OrderBasic OB WHERE OB.memberid = ?1", nativeQuery = true)
//    Page<OrderBasic> findOrderBasicByMemberid(Integer memberid, Pageable pageable);

    @Query("SELECT ob.statusid.id FROM OrderBasic ob WHERE ob.id = :orderId")
    Integer findStatusId_IdByOrderId(Integer orderId);


    @Query(value = "SELECT COALESCE(SUM(ob.totalamount), 0) FROM dbo.OrderBasic ob WHERE YEAR(ob.merchanttradedate) = :year AND ob.sellerid = :memberId AND ob.statusid = 4", nativeQuery = true)
    Integer findTotalAmountByYearAndSeller(@Param("year") String yearAsString, @Param("memberId") Integer memberId);
    @Query(value = "SELECT COALESCE(SUM(ob.totalamount), 0) FROM dbo.OrderBasic ob WHERE YEAR(ob.merchanttradedate) = :year AND MONTH(ob.merchanttradedate) = :month AND ob.sellerid = :memberId AND ob.statusid = 4", nativeQuery = true)
    Integer findTotalAmountByYearAndMonthAndSeller(@Param("year") Integer year, @Param("month") Integer month,
                                                   @Param("memberId") Integer memberId);
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


    //20231103 新增
    //買家找一筆訂單 by orderid 與 memberid
    @Query(value = "select * from OrderBasic where memberid = ?2 and orderid = ?1",nativeQuery = true)
    OrderBasic findOrderBasicByIdandMemberid(Integer orderid, int memberid);

    //20231104 新增
    //賣家找訂單 By 賣家與 訂單狀態
    @Query(value = "SELECT * FROM OrderBasic OB WHERE OB.sellerid = ?1 AND OB.statusid = ?2", nativeQuery = true)
    Page<OrderBasic> findOrderBasicByStatusAndSeller( Integer sellerid,  Integer statusid, Pageable pageable);

    //20231103 新增
    //賣家找一筆訂單 by orderid 與 seller
    @Query(value = "select * from OrderBasic where sellerid = ?2 and orderid = ?1",nativeQuery = true)
    OrderBasic findOrderBasicByIdandSellerid(Integer orderid, int sellerid);




}
