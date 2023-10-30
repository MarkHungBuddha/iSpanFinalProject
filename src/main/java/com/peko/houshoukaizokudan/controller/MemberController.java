package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.ProductImageService;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService userUservice;
	@Autowired
	private ProductImageService imageUservice;
	


	
	
	@PostMapping("/public/api/member/post")
	public Map<String, String> postRegister(
			@RequestParam("username") String username,
			@RequestParam("passwdbcrypt") String password,
			@RequestParam("membertypeid") MemberType membertypeid,
//			@RequestParam("memberimgpath") String memberimgpath,
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
		Map<String, String> response = new HashMap<>();
		boolean isExist = userUservice.checkIfUsernameExist(username);
		
		if(isExist) {
			response.put("errorMsg", "此帳號已存在，請用別的");
		} else {
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
			u1.setMemberimgpath("nnNjLVE");
			u1.setMembertypeid(membertypeid);
			u1.setPhone(phone);
			u1.setPostalcode(postalcode);
			u1.setRegion(region);
			u1.setStreet(street);
			
			userUservice.addUser(u1);
			response.put("okMsg", "註冊成功");
		}
		
		return response;
	}
	
	@PostMapping("/public/api/member/memberLogin")
	public Map<String, String> checkUserLogin(
			@RequestParam("username") String username, 
			@RequestParam("passwdbcrypt") String password,
			HttpSession httpsession,
			Model model) {
		Map<String, String> response = new HashMap<>();
		Member result = userUservice.checkLogin(username, password);
		
		if(result != null) {
			httpsession.setAttribute("loginUser", result);
			response.put("success", "登入成功");
		} else {
			response.put("error", "帳號密碼錯誤");
		}
		
		return response;
	}
	
	@PostMapping("/customer/member/logout")
	public Map<String, String> logout(HttpSession httpsession) {
	    Map<String, String> response = new HashMap<>();
	    
	    httpsession.removeAttribute("loginUser");
	    
	    httpsession.invalidate();
	    
	    response.put("success", "登出成功");
	    
	    return response;
	}
	   @GetMapping("/public/api/member/{id}")
	    public Member getUserProfile(@PathVariable Integer id) {
	        Member user = userUservice.findById(id);
	        return user;
	    }
	   @PutMapping("/public/api/member/update/{id}")
	    public Map<String, String> updateUserProfile(
	        @PathVariable Integer id,
	        @RequestBody Member updatedUser) {
	        Map<String, String> response = new HashMap<>();
	        try {
	            Member existingUser = userUservice.findById(id);
	            // 在这里更新用户详细信息
	            existingUser.setUsername(updatedUser.getUsername());
	            existingUser.setFirstname(updatedUser.getFirstname());
	            existingUser.setLastname(updatedUser.getLastname());
	            existingUser.setCity(updatedUser.getCity());
	            existingUser.setCountry(updatedUser.getCountry());
	            existingUser.setGender(updatedUser.getGender());
	            existingUser.setPhone(updatedUser.getPhone());
	            existingUser.setPostalcode(updatedUser.getPostalcode());
	            existingUser.setRegion(updatedUser.getRegion());
	            existingUser.setStreet(updatedUser.getStreet());
	            // 根据需要设置其他字段

	            userUservice.updateMember(existingUser);
	            response.put("success", "更新成功");
	        } catch (Exception e) {
	            response.put("error", "更新失敗");
	        }
	        return response;
	    }
	   @GetMapping("/public/api/checkLoginStatus")
	   public ResponseEntity<Map<String, Object>> checkLoginStatus(HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    Member loggedInUser = (Member) session.getAttribute("loginUser");

	    if (loggedInUser != null) {
	     response.put("isLoggedIn", true);
	     Integer typeId = loggedInUser.getMembertypeid().getId(); // 假設MemberType有一個getId方法來獲取ID

	     switch (typeId) {
	      case 1:
	       response.put("role", "超級管理員");
	       break;
	      case 2:
	       response.put("role", "賣家");
	       break;
	      case 3:
	       response.put("role", "買家");
	       break;
	      default:
	       response.put("role", "未知角色");
	     }
	    } else {
	     response.put("isLoggedIn", false);
	    }
	    return ResponseEntity.ok(response);
	   }
	// 其他控制器方法和功能
}
