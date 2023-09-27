package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductReviewData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReviewData, Integer> {
}
