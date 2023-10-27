package com.peko.houshoukaizokudan.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;

	public List<OrderBasic> findOrderBasicDataBymemberid(Member buyer) {
//		Integer Member= buyer.getId();
		
		List<OrderBasic> orders =orderRepo.findOrderBasicBybuyer(buyer);
		
		if(orders.isEmpty()) {
			return null;
			}

		return orders;
	}

	public Integer findTotalByYear(Integer memberIdd, Integer year) {
		Integer obb=orderRepo.findTotalAmountByYearAndSeller(memberIdd, year);
		
		return obb;
	}

	public Integer findTotalByMonth(Integer memberIdd, Integer month, Integer year) {
Integer obb=orderRepo.findTotalAmountByYearAndMonthAndSeller(year,memberIdd, month);
		
		return obb;
	}
}
