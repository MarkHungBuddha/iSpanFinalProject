package com.peko.houshoukaizokudan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	//回首頁
	@GetMapping("/index")
	public String home() {
		System.out.println("Go To Home");
		return "index";
	}

}