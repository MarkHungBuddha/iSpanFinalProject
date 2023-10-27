package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {


    List<ProductReview> findByProductid_Id(Integer productId);




}
