package com.peko.houshoukaizokudan.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

	List<ProductImage> findProductImageDataByimagepathLike(String imagepath);
	
}
