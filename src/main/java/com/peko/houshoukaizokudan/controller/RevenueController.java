package com.peko.houshoukaizokudan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.RevenueDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.OrderBasicService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class RevenueController {

	@Autowired
	private OrderBasicService obService;

	// 賣家顯示年營收
	@GetMapping("/seller/api/revenue/year")
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

	// 賣家顯示月營收
	@GetMapping("/seller/api/revenue/month")
	public Integer findByMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		System.out.println("月月" + month);
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();

			Integer ob = obService.findTotalByMonth(year, month, memberIdd);

			return ob;
		}
		return null;

	}

	@GetMapping("/seller/api/revenue/yearEachMonth")
	public List<RevenueDto> findAllMonth(@RequestParam("year") Integer year, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {
			Integer memberIdd = loginUser.getId();

			List<RevenueDto> ob = obService.findTotalAllMonth(year, memberIdd);
			System.out.println(ob);
			return ob;
		}
		return null;

	}

}
