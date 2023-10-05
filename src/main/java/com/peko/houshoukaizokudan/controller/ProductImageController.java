package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProductImageController {


    @Autowired
    private ProductImageService productImageService;
}
