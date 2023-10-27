package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Transactional
    public List<String> findImageByProductId(Integer productId) {
        return productImageRepository.findImagePathsByProductIdOrderByOrderID(productId);
    }
}
