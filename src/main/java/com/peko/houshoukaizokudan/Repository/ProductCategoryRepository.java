package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryData, Integer> {
}
