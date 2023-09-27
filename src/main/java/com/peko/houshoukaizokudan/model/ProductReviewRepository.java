package com.peko.houshoukaizokudan.model;

import com.peko.houshoukaizokudan.model.ProductReviewData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReviewData, Integer> {
}
