package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
}
