package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    Optional<ProductCategory> findById(Integer id);
}
