package com.peko.houshoukaizokudan.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peko.houshoukaizokudan.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>{
	Optional<ProductCategory> findById(Integer id);
}
