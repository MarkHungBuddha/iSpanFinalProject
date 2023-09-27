package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageData, Integer> {
}
