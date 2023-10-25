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

public interface OrderDetailRepository extends JpaRepository<OrderBasic, Integer> {

	//找訂單的商品
	@Query(value = "SELECT * FROM OrderDetail OD WHERE OD.orderid = ?1", nativeQuery = true)
	List<OrderDetail> findOrderDetailByOrderid(Integer orderid);
}
