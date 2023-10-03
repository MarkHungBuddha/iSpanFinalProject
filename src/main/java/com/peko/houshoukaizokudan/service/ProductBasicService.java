package com.peko.houshoukaizokudan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductBasicRepository;

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
	
	

	
	
	//頁碼  //1頁2筆

	public Page<ProductBasic> findProductByPage(Integer pageNumber){
		Pageable pgb= PageRequest.of(pageNumber-1, 3,Sort.Direction.ASC, "id");
		Page<ProductBasic> page = prdRepo.findAll(pgb);
		return page;
	}
	
//	public ProductBasic findLastest() {
//		return prdRepo.findFirstByOrderIdDesc();
//	}

}
