package com.peko.houshoukaizokudan.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ProductBasic;



public interface ProductBasicRepository extends JpaRepository<ProductBasic, Integer>{

	List<ProductBasic> findBySellermemberid(Integer sellermemberid);
	}
