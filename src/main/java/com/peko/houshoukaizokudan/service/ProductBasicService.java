package com.peko.houshoukaizokudan.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.repository.ProductBasicRepository;

@Service
public class ProductBasicService {

	@Autowired
	private ProductBasicRepository pbRepo;
	
	public void insert(ProductBasic pb) {
		pbRepo.save(pb);
	}

	public List<ProductBasic> findBySellerMemberId(Integer sellermemberid) {
	    List<ProductBasic> pbList = pbRepo.findBySellermemberid(sellermemberid);
	    return pbList;
	}
}
