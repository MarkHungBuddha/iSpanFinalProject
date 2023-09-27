package com.peko.houshoukaizokudan.model;

import com.peko.houshoukaizokudan.model.ProductCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryData, Integer> {
}
