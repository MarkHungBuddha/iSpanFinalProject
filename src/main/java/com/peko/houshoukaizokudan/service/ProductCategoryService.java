package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ParentCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import com.peko.houshoukaizokudan.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;







	public ProductCategory findById(Integer id) {
		Optional<ProductCategory> optioanl = productCategoryRepository.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;

	}
//	public     ProductCategory findByCategoryId(Integer categoryId); {
//		
//	}

}
