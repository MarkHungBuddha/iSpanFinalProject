package com.peko.houshoukaizokudan.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.repository.ProductCategoryRepository;

@Service
public class ProductCategoryService {

	@Autowired
	private ProductCategoryRepository pcRepo;

	public ProductCategory findById(Integer id) {
		Optional<ProductCategory> optioanl = pcRepo.findById(id);

		if (optioanl.isPresent()) {
			return optioanl.get();
		}

		return null;

	}
}
