package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
}
