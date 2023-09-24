package com.peko.houshoukaizokudan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ProductBasicService;

@Controller
public class ProductController {

    @Autowired
    private ProductBasicService prdService;
    
    
	//跳頁
	@GetMapping("/product/productFind")
	public String productFindPage() {
		return "product/productFindPage";
	}
    
    @PostMapping("/product/productFind")
    public String productFind(@RequestParam("productName") String productname, Model model) {
        List<ProductBasic> products = prdService.findProductBasicDataByproductname(productname);
        
        model.addAttribute("products", products);
        return "product/productFindPage"; 
    }
}
