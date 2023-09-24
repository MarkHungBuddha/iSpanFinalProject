package com.peko.houshoukaizokudan.service;

import com.peko.houshoukaizokudan.model.ProductBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBasicService {

    @Autowired
    private ProductBasicRepository productBasicRepository;
}
