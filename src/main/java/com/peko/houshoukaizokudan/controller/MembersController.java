package com.peko.houshoukaizokudan.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.MemberType;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.MemberTypeService;

import jakarta.servlet.http.HttpSession;



@Controller
public class MembersController {


	@Autowired
	private MemberService mService;
	
	@Autowired
	private MemberTypeService mtService;
	
	@GetMapping("/member/register")
	public String register() {
		return "member/registerPage";
	}
	
	@PostMapping("/member/post")
	public String postRegister(
			@RequestParam("membertypeid") Integer membertypeid,
			@RequestParam("memberimgpath") String memberimgpath,
			@RequestParam("username") String username,
			@RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname,
			@RequestParam("gender") String gender,
			@RequestParam("passwdbcrypt") String passwdbcrypt,
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
		boolean isExist = mService.checkIfUsernameExist(username);
		if(isExist) {
			System.out.println("帳號已註冊");
			model.addAttribute("errorMsg","此帳號已存在");
		}else {
			Member m1=new Member();
			MemberType memberType = mtService.findById(membertypeid);
			m1.setMembertypeid(memberType);		
			m1.setMemberimgpath(memberimgpath);
			m1.setUsername(username);
			m1.setFirstname(firstname);
			m1.setLastname(lastname);
			m1.setGender(gender);
			m1.setPasswdbcrypt(passwdbcrypt);
			m1.setBirthdate(birthdate);
			m1.setPhone(phone);
			m1.setEmail(email);
			m1.setMembercreationdate(membercreationdate);
			m1.setCountry(country);
			m1.setCity(city);
			m1.setRegion(region);
			m1.setStreet(street);
			m1.setPostalcode(postalcode);
			mService.addMember(m1);
			System.out.println("註冊成功");
			model.addAttribute("okMsg","註冊成功");
		}
		return "member/registerPage";
	}
	
	@GetMapping("/member/login")
	public String userLoginPage() {
		return "member/loginPage";
		
	}

	@PostMapping("/member/login")
	public String checkUserLogin(
			@RequestParam("username") String username,
			@RequestParam("passwdbcrypt") String passwdbcrypt,
			HttpSession httpSession,
			Model model) {
		
		Member result = mService.checkLogin(username, passwdbcrypt);
		
		if(result != null) {
			System.out.println("登入成功");
			httpSession.setAttribute("loginUser", result);
			System.out.println("OK");
		}else {
			System.out.println("登入失敗");
			model.addAttribute("loginFail","帳號密碼錯誤");
			System.out.println("NOK");
		}
		return"member/loginOK";
	}
}
