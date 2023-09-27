package com.peko.houshoukaizokudan.model;

import com.peko.houshoukaizokudan.model.ProductBasicData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBasicRepository extends JpaRepository<ProductBasicData, Integer> {
}
