package com.peko.houshoukaizokudan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.Repository.OrderStatusRepository;
import com.peko.houshoukaizokudan.model.OrderStatus;

@Service
public class OrderStatusService {
	@Autowired
	private OrderStatusRepository orderStatusRepo;

	public OrderStatus findOrderStatusById(Integer id) {
		OrderStatus OrderStatus = orderStatusRepo.findOrderStatusById(id);
		return OrderStatus;
	}


}
