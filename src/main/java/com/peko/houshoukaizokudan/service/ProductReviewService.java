package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.DTO.ProductReviewDTO;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductReviewService {

    @Autowired
    ProductReviewRepository productReviewRepository;

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
}
