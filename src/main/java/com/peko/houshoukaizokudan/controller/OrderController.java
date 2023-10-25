package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peko.houshoukaizokudan.DTO.ProductItem;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.model.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.service.OrderBasicService;

import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {

	@Autowired
	private OrderBasicService orderService;

	// 跳頁
	@GetMapping("/orders/orderBase")
	public String orderShowPage() {
		return "order/showOrders";
	}

	@PostMapping("/orders/orderBase")
	public String orderShow(HttpSession session, Model model) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    
	    if (loginUser != null) {
	        List<OrderBasic> orders = orderService.findOrderBasicDataBymemberid(loginUser);
	        model.addAttribute("orders", orders);
	        return "order/showOrders";
	    } else {
	        // 如果会话中没有登录用户信息，可以重定向到登录页面或采取其他操作
	        return "order/showOrders";
	    }
	}

//	@PostMapping("/api/checkout")
//	public ResponseEntity<?> checkout(@RequestBody checkoutOrderDto orderDto) {
//		Set<ProductItem> items = orderDto.getProductItems();
//
//		// 進行結帳的相關操作...
//		// 例如，您可以使用 orderDto.getTotalPrice() 來獲取總價
//		double totalPrice = orderDto.getTotalPrice();
//
//		return ResponseEntity.ok().build();
//	}

	@PostMapping("/api/order/checkout")
	public ResponseEntity<?> checkout(@RequestBody checkoutOrderDto orderDto) {
		Set<ProductItem> items = orderDto.getProductItems();
		double totalPrice = 0.0;
		double totalRating = 0.0;
		int totalReviewedProducts = 0;

		for(ProductItem item : items) {
			ProductBasic product = item.getProduct();
			if(product == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product not found.");
			}

			// 检查库存
			if(product.getQuantity() < item.getQuantity()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient stock for product " + product.getProductname());
			}

			// 计算总价
			if(product.getSpecialprice() != null && product.getSpecialprice().compareTo(BigDecimal.ZERO) > 0) {
				totalPrice += product.getSpecialprice().doubleValue() * item.getQuantity();
			} else {
				totalPrice += product.getPrice().doubleValue() * item.getQuantity();
			}

			// 计算总平均评价
			Set<ProductReview> reviews = product.getProductReview();
			if(reviews.size() > 0) {
				for(ProductReview review : reviews) {
					totalRating += review.getRating();
				}
				totalReviewedProducts++;
			}
		}

		double averageReview = (totalReviewedProducts > 0) ? totalRating / totalReviewedProducts : 0;

		// 返回结果
		Map<String, Object> result = new HashMap<>();
		result.put("totalPrice", totalPrice);
		result.put("averageReview", averageReview);
		return ResponseEntity.ok(result);
	}


}





