package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {

	List<OrderBasic> findOrderBasicBybuyer(Member buyer);

	//買家找訂單(Page)
	Page<OrderBasic> findOrderBasicBybuyer(Member buyer, Pageable Pageable);
	
	//買家找訂單狀態
//	@Query(value = "SELECT * FROM OrderBasic OB INNER JOIN OrderStatus OS ON OB.statusID = OS.statusid WHERE OB.memberid = ?1 AND OS.statusID = ?2", nativeQuery = true)
	@Query(value = "SELECT * FROM OrderBasic OB WHERE OB.memberid = ?1 AND OB.statusid = ?2", nativeQuery = true)
	Page<OrderBasic> findOrderBasicByStatus( Integer memberid,  Integer statusid, Pageable pageable);

	OrderBasic findOrderBasicByOrderid(int orderid);


}
