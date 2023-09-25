package com.peko.houshoukaizokudan.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderBasicRepository;
import com.peko.houshoukaizokudan.model.ProductBasic;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;

	public List<OrderBasic> findOrderBasicDataBymemberid(Member member) {
		
		List<OrderBasic> orders =orderRepo.findOrderBasicDataBymemberid(member);
		
		if(orders.isEmpty()) {
			return null;
			}

		return orders;
	}
}
