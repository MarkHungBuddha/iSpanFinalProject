package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ParentCategoryRepository;
import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    //新增商品分類






	public ProductCategory findById(Integer id) {
		Optional<ProductCategory> optioanl = pcRepo.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;

	}
}
