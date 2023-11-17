package com.peko.houshoukaizokudan.Repository;

import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {


    List<ProductReview> findByProductid_Id(Integer productId);


    boolean existsByIdAndMemberid_Id(Integer reviewId, Integer memberId);



    @Query("SELECT pr FROM ProductReview pr JOIN pr.productid p WHERE p.sellermemberid.id = :sellerId")
    Page<ProductReview> findRecentReviewsBySellerId(@Param("sellerId") Integer sellerId, Pageable pageable);

    @Query("SELECT AVG(pr.rating) FROM ProductReview pr WHERE pr.productid.id = :productId")
    Double findAverageRatingByProductId(Integer productId);

    Long countByProductid_Id(Integer productId);

    @Query(value = "SELECT FORMAT(CAST(reviewtime AS DATETIME), 'yyyy-MM') as month, COUNT(reviewid) as reviewsCount, AVG(rating) as averageRating " +
            "FROM ProductReview " +
            "WHERE YEAR(CAST(reviewtime AS DATETIME)) = YEAR(GETDATE()) AND memberid = :sellerId " +
            "GROUP BY FORMAT(CAST(reviewtime AS DATETIME), 'yyyy-MM') " +
            "ORDER BY month DESC", nativeQuery = true)
    List<Object[]> findMonthlyReviewsStatsForSeller(Integer sellerId);

    @Query(value = "SELECT * FROM product_review WHERE productid IN ?2", nativeQuery = true)
    List<ProductReview> findProductReviewsByProductid(Integer productid);

    @Query(value = "SELECT CASE " +
            "WHEN EXISTS (" +
            "SELECT 1 " +
            "FROM dbo.ProductReview AS pr " +
            "WHERE pr.orderdetailid = ?1 AND pr.memberid = ?2" +
            ") " +
            "THEN CAST(1 AS BIT) " +
            "ELSE CAST(0 AS BIT) " +
            "END", nativeQuery = true)
    boolean hasBuyerReviewed(Integer orderDetailId, Integer memberId);


    @Query("SELECT pr FROM ProductReview pr WHERE pr.productid.sellermemberid.id = :sellerId")
    List<ProductReview> findByProductid_Seller_Id(Integer sellerId);

    @Query(value = "SELECT * FROM ProductReview  WHERE memberid = ?1",nativeQuery = true)
    List<ProductReview> findByMemberid_Id(Integer buyerId);







}
