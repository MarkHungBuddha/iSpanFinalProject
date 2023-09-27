package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;
}
