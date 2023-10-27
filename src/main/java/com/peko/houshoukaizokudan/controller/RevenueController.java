package com.peko.houshoukaizokudan.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.model.Member;

import com.peko.houshoukaizokudan.service.OrderBasicService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class RevenueController {

	@Autowired
	private OrderBasicService obService;

	@GetMapping
	public Integer findByYear(@RequestParam("year") Integer year, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();

			Integer ob = obService.findTotalByYear(memberIdd, year);

			return ob;
		}
		return null;

	}

	@GetMapping
	public Integer findByMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();

			Integer ob = obService.findTotalByMonth(year, memberIdd, month);

			return ob;
		}
		return null;

	}

}