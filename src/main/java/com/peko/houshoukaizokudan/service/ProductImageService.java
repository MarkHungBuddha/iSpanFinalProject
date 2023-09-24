package com.peko.houshoukaizokudan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.ProductImage;
import com.peko.houshoukaizokudan.model.ProductImageRepository;

@Service
public class ProductImageService {
	
	@Autowired
	private ProductImageRepository imageRepo;
	
	public List<ProductImage> findProductImageDataByimagepath(String imagepath){
		
		List<ProductImage> images = imageRepo.findProductImageDataByimagepathLike(imagepath);
		
		if(images.isEmpty()) {
			return null; 
		}
		return images;
	}

}
