package com.peko.houshoukaizokudan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{

}