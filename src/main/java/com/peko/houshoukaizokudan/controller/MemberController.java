package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService userUservice;
	
	@GetMapping("/member/memberRe")
	public String register() {
		return "memberRegister";
	}
	
	@PostMapping("/member/post")
	public String postRegister(
			@RequestParam("username") String username,
			@RequestParam("passwdbcrypt") String password,
			@RequestParam("membertypeid") MemberType membertypeid,
			@RequestParam("memberimgpath") String memberimgpath,
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam("gender") String gender,
			@RequestParam("birthdate") String birthdate,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("membercreationdate") String membercreationdate,
			@RequestParam("country") String country,
			@RequestParam("city") String city,
			@RequestParam("region") String region,
			@RequestParam("street") String street,
			@RequestParam("postalcode") String postalcode,

			Model model) {
		boolean isExist = userUservice.checkIfUsernameExist(username);
		
		if(isExist) {
			model.addAttribute("errorMsg", "此帳號已存在，請用別的");
		}else {
			Member u1 = new Member();
			u1.setUsername(username);
			u1.setPasswdbcrypt(password);
			u1.setBirthdate(birthdate);
			u1.setMembertypeid(membertypeid);
			u1.setCity(city);
			u1.setCountry(country);
			u1.setEmail(email);
			u1.setFirstname(firstname);
			u1.setGender(gender);
			u1.setLastname(lastname);
			u1.setMembercreationdate(membercreationdate);
			u1.setMemberimgpath(memberimgpath);
			u1.setMembertypeid(membertypeid);
			u1.setPhone(phone);
			u1.setPostalcode(postalcode);
			u1.setRegion(region);
			u1.setStreet(street);
			
			userUservice.addUser(u1);
			System.out.println("註冊成功");
			model.addAttribute("okMsg", "註冊成功");
		}
		
		return "member/memberLogin";
	}
	
	@GetMapping("/member/memberLogin")
	public String userLoginPage() {
		return "member/memberLogin";
	}
	
	@PostMapping("/member/memberLogin")
	public String checkUserLogin(
			@RequestParam("username") String username, 
			@RequestParam("passwdbcrypt") String password,
			HttpSession httpsession,
			Model model) {
		Member result = userUservice.checkLogin(username, password);
		
		if(result != null) {
			System.out.println("登入成功");
			httpsession.setAttribute("loginUser", result);
		}else {
			System.out.println("登入fail");
			model.addAttribute("loginFail", "帳號密碼錯誤");
		}
		
		return "member/memberLogin";
	}

	//登出

	//刪除帳號

}