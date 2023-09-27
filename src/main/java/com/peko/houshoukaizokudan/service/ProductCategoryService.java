package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.Repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
}
