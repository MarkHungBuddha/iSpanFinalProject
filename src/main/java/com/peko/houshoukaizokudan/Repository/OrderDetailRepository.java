package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

	//找訂單的商品
	@Query(value = "SELECT * FROM OrderDetail  WHERE orderid = ?1", nativeQuery = true)
	List<OrderDetail> findOrderDetailByOrderid(Integer orderid);

	
	//找訂單商品的數量
	@Query(value = "SELECT quantity FROM OrderDetail  WHERE orderid = ?1 and productid = ?2", nativeQuery = true)
	Integer getProductQuantityFromorderDetail(Integer orederid, Integer productid);
}
