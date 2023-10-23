package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {

	List<OrderBasic> findOrderBasicBybuyer(Member buyer);

	OrderBasic findOrderBasicByOrderid(int orderid);
	
//	@Query() //還沒寫
//	List<OrderBasic> saveAll(List<ProductBasic> buyProducts);


}
