package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductBasicRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBasicService {

    @SuppressWarnings("unused")
	@Autowired
    private ProductBasicRepository productBasicRepository;

	public List<ProductBasic> findProductBasicDataByProductName(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	
}