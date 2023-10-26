package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
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





	@PostMapping("/api/order/checkout")
	public ResponseEntity<checkoutOrderDto> checkout(@RequestBody List<ProductIDandQuentity> productItems, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser"); // assuming you stored user ID in session
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		System.out.println(productItems.toString());
		try {
			System.out.println("checkoutOrderDto orderDto");
			checkoutOrderDto orderDto = orderService.processCheckout(loginUser, productItems);
			System.out.println("return ResponseEntity.ok(orderDto);");
			return ResponseEntity.ok(orderDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


}





