package com.peko.houshoukaizokudan.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBasicRepository extends JpaRepository<OrderBasic, Integer> {

	List<OrderBasic> findOrderBasicBybuyer(Member buyer);
}
