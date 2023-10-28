package com.peko.houshoukaizokudan.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer>{

	OrderStatus findOrderStatusById(Integer id);
}
