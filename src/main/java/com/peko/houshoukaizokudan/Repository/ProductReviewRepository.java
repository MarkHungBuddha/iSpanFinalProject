package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    @Query(value = "SELECT * FROM product_review WHERE productid IN ?2", nativeQuery = true)
    List<ProductReview> findProductReviewsByProductid(Integer productid);

}
