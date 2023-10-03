package com.peko.houshoukaizokudan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.repository.ProductBasicRepository;

@Service
public class ProductBasicService {
	
	@Autowired
	private ProductBasicRepository prdRepo;
	
	
	public List<ProductBasic> findProductBasicDataByproductname(String productname) {	
	
		
		List<ProductBasic> products =prdRepo.findProductBasicDataByproductnameLike("%"+productname+"%");
		
		if(products.isEmpty()) {
			return null; 
		}
		return 	products;
		
	};
	

}
