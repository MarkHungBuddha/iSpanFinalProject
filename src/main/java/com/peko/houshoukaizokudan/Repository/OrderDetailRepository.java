package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    boolean existsByIdAndProductid_Id(Integer orderDetailId, Integer productId);

    //找訂單的商品
    @Query(value = "SELECT * FROM OrderDetail  WHERE orderid = ?1", nativeQuery = true)
    List<OrderDetail> findOrderDetailByOrderid(Integer orderid);


    //找訂單商品的數量
    @Query(value = "SELECT quantity FROM OrderDetail  WHERE orderid = ?1 and productid = ?2", nativeQuery = true)
    Integer getProductQuantityFromorderDetail(Integer orederid, Integer productid);

    //找訂單明細ID by 訂單id 產品id  //20231104 新增
    @Query(value = "SELECT * FROM OrderDetail  WHERE orderid = ?1 and productid = ?2", nativeQuery = true)
    Integer findIdByOrderid_IdAndProductid_Id(Integer orderid, Integer productid);
}