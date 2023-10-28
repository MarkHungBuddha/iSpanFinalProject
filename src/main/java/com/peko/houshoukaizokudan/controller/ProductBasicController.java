package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;

import java.util.List;


import com.peko.houshoukaizokudan.DTO.ProductBasicDto;
import com.peko.houshoukaizokudan.DTO.ProductCategoryDto;
import com.peko.houshoukaizokudan.DTO.ProductDto;
import com.peko.houshoukaizokudan.handler.InvalidPriceRangeException;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;



import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.ProductBasicService;
import com.peko.houshoukaizokudan.service.ProductCategoryService;

@RestController
@Controller
public class ProductBasicController {

    @Autowired
    private ProductBasicService prdService;
    
    @Autowired
    private ProductCategoryService productCategoryService; // 确保已注入 ProductCategoryService


	//跳頁
	@GetMapping("/product/productFind")
	public String productFindPage() {
		return "product/productFindPage";
	}
//    
//	//模糊搜尋 存頁碼跟關鍵字 ** 此功能目前已不使用 **
//	//未給頁碼 預設第一頁
//	@ResponseBody
//	@GetMapping("/products")
//	public Page<ProductDto> getProductsByPage(
//		@RequestParam(name = "productname", required = false) String productname,
//	    @RequestParam(name = "p", defaultValue = "1") Integer page) {
//	    Pageable pageable = PageRequest.of(page -1, 5); // 每頁 5 項
//	    Page<ProductDto> result = prdService.getProductsByPage(pageable, productname);
//	    return result;
//	}
	
	//模糊搜尋 + 價格範圍
	//未給頁碼 預設第一頁
	@GetMapping("/api/products")
	public ResponseEntity<Page<ProductDto>> getProductsByPage(
		@RequestParam(name = "productname", required = false) String productname,
		@RequestParam(value = "minPrice", required = false, defaultValue = "0.0") Double minPrice,
	    @RequestParam(value = "maxPrice", required = false, defaultValue = "999999.99") Double maxPrice,
	    @RequestParam(name = "page", required = false,defaultValue = "1") Integer page) {
	    Pageable pageable = PageRequest.of(page -1, 5); // 每頁 5 項
	    
	    
	    try {
	    	Page<ProductDto> result = prdService.getProductsByPage(productname, minPrice, maxPrice, pageable);
        	return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (InvalidPriceRangeException e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
	
	
	

	//價格範圍搜尋 + 分類名稱(categoryname)搜尋
	@GetMapping("/api/categoryname")
	public ResponseEntity<Page<ProductCategoryDto>> getCategoryNameByPriceRange(
	        @RequestParam(value = "categoryname", required = true) String categoryname, //categoryname(分類名稱)必填
	        @RequestParam(value = "minPrice", required = false, defaultValue = "0.0") Double minPrice,
	        @RequestParam(value = "maxPrice", required = false, defaultValue = "999999.99") Double maxPrice,
	        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
			Pageable pageable = PageRequest.of(page -1, 5); //第一頁是0 所以-1 ，每頁 5 項
			
	    try {
	        Page<ProductCategoryDto> result = prdService.getCategoryNameByPriceRange(categoryname, minPrice, maxPrice, pageable);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (InvalidPriceRangeException e) {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}

	


}