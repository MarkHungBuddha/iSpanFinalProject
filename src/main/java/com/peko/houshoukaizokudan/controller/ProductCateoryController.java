package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ProductCateoryController {

    @Autowired
    private ProductCategoryService productCategoryService;
}