package com.peko.houshoukaizokudan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.service.OrderBasicService;

import jakarta.servlet.http.HttpSession;

@Controller
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


}
