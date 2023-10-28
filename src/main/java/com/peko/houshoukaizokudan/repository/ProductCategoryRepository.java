package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}
