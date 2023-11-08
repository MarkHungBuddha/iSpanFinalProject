package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductReviewDTO;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;
import com.peko.houshoukaizokudan.Repository.OrderDetailRepository;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductReviewService {

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderBasicRepository orderBasicRepository;
    @Autowired
    private ProductBasicRepository productBasicRepository;

    @Transactional
    public List<ProductReviewDTO> findProductReviewByProductid(Integer productid){
        List<ProductReview> productReviewList=productReviewRepository.findByProductid_Id(productid);
        List<ProductReviewDTO> productReviewDTOList = new ArrayList<>();
        for(ProductReview productReview:productReviewList){
            ProductReviewDTO productReviewDTO=new ProductReviewDTO();
            productReviewDTO.setProductid(productReview.getProductid().getId());
            productReviewDTO.setMemberid(productReview.getMemberid().getId());
            productReviewDTO.setRating(productReview.getRating());
            productReviewDTO.setReviewcontent(productReview.getReviewcontent());
            productReviewDTO.setReviewtime(productReview.getReviewtime());
            assert productReviewDTOList != null;
            productReviewDTOList.add(productReviewDTO);

        }
        return productReviewDTOList;


    }

    public boolean isOrderBelongToMember(Integer orderId, Integer memberId) {
        return orderBasicRepository.existsByIdAndBuyer_Id(orderId, memberId);
    }

    public Integer getOrderDetailIdByOrderIdAndProductId(Integer orderId, Integer productId) {

        return orderDetailRepository.findIdByOrderid_IdAndProductid_Id(orderId, productId);

    }



    public boolean isProductInOrder(Integer orderDetailId, Integer productId) {
        if(orderDetailRepository.findStatusIdByOrderDetailId(orderDetailId)==4)
            return orderDetailRepository.existsByIdAndProductid_Id(orderDetailId, productId);
        return false;
    }

    public boolean isOrderStatusFinish(Integer orderId){
        Integer statusId = orderBasicRepository.findStatusId_IdByOrderId(orderId);
        return statusId != null && statusId.equals(4);
    }

    @Transactional
    public void createReview(ProductReviewDTO productReviewDTO, Member loginUser) {
        ProductReview productReview = new ProductReview();
        productReview.setRating(productReviewDTO.getRating());
        productReview.setReviewcontent(productReviewDTO.getReviewcontent());
        productReview.setReviewtime(productReviewDTO.getReviewtime());
        productReview.setMemberid(loginUser);
        productReview.setProductid(productBasicRepository.findById(productReviewDTO.getProductid()).orElse(null));
        productReview.setOrderdetail(orderDetailRepository.findById(productReviewDTO.getOrderdetailid()).orElse(null));
        // 设置其他字段
        productReviewRepository.save(productReview);
    }

    public boolean isReviewBelongToMember(Integer reviewId, Integer memberId) {
        return productReviewRepository.existsByIdAndMemberid_Id(reviewId, memberId);
    }

    @Transactional
    public void updateReview(Integer id, ProductReviewDTO productReviewDTO) {
        ProductReview productReview = productReviewRepository.findById(id).orElse(null);
        if(productReview != null) {
            productReview.setRating(productReviewDTO.getRating());
            productReview.setReviewcontent(productReviewDTO.getReviewcontent());
            // 更新其他字段
            productReviewRepository.save(productReview);
        }
    }

    public boolean hasBuyerReviewed(Integer buyerId,Integer orderDetailid){
        System.out.println("loginUserID="+buyerId);
        System.out.println("orederDetailid:"+orderDetailid);

    	return productReviewRepository.hasBuyerReviewed(orderDetailid,buyerId);
    }

    public List<ProductReview> getRecentReviewsForSeller(Integer sellerId, Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return productReviewRepository.findRecentReviewsBySellerId(sellerId, pageable).getContent();
    }

    public Map<String, Object> getProductAverageReview(Integer productId) {
        // 这里我们计算平均评价和评论数量
        Double averageRating = productReviewRepository.findAverageRatingByProductId(productId);
        Long reviewCount = productReviewRepository.countByProductid_Id(productId);
        Map<String, Object> result = new HashMap<>();
        result.put("averageRating", averageRating);
        result.put("reviewCount", reviewCount);
        return result;
    }

    public Map<String, Object> getSellerMonthlyReview(Integer sellerId) {
        List<Object[]> monthlyStats = productReviewRepository.findMonthlyReviewsStatsForSeller(sellerId);

        Map<String, Object> monthlyData = new HashMap<>();

        for (Object[] stat : monthlyStats) {
            String month = (String) stat[0];
            Long reviewCount = (Long) stat[1];
            Double averageRating = (Double) stat[2];

            Map<String, Object> dataForMonth = new HashMap<>();
            dataForMonth.put("reviewCount", reviewCount);
            dataForMonth.put("averageRating", averageRating);

            monthlyData.put(month, dataForMonth);
        }

        return monthlyData;
    }
}
