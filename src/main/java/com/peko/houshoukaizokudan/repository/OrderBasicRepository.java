package com.peko.houshoukaizokudan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {

	List<OrderBasic> findOrderBasicBybuyer(Member buyer);

	OrderBasic findOrderBasicByOrderid(int orderid);


}
