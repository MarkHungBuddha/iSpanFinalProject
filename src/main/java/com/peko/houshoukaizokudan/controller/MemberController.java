package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.MemberDTO;
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
			u1.setEmail(email);
			u1.setFirstname(firstname);
			u1.setLastname(lastname);
			u1.setGender(gender);
			u1.setCountry(country);
			u1.setPostalcode(postalcode);
			u1.setCity(city);
			u1.setRegion(region);
			u1.setStreet(street);
			u1.setPhone(phone);
			u1.setMemberimgpath("nnNjLVE");
			u1.setMembercreationdate(membercreationdate);
			
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
	public MemberDTO getUserProfile(@PathVariable Integer id) {
	    Member user = userUservice.findById(id);
	    
	    if (user != null) {
	        MemberDTO userDTO = new MemberDTO();
	        userDTO.setId(user.getId());
	        userDTO.setMemberimgpath(user.getMemberimgpath());
	        userDTO.setUsername(user.getUsername());
	        userDTO.setFirstname(user.getFirstname());
	        userDTO.setLastname(user.getLastname());
	        userDTO.setGender(user.getGender());
	        userDTO.setBirthdate(user.getBirthdate());
	        userDTO.setPhone(user.getPhone());
	        userDTO.setEmail(user.getEmail());
	        userDTO.setMembercreationdate(user.getMembercreationdate());
	        userDTO.setCountry(user.getCountry());
	        userDTO.setCity(user.getCity());
	        userDTO.setRegion(user.getRegion());
	        userDTO.setStreet(user.getStreet());
	        userDTO.setPostalcode(user.getPostalcode());
	        
	        // 设置 membertypeid 和相关字段
	        userDTO.setMembertypeid(user.getMembertypeid().getId());
	        userDTO.setMembertypename(user.getMembertypeid().getMembertypename());
	        userDTO.setMemberTypeDescription(user.getMembertypeid().getMemberTypeDescription());
	        
	        return userDTO;
	    } else {
	        // 处理找不到用户的情况，可以返回 null 或其他适当的响应
	        return null;
	    }
	}
	@PutMapping("/public/api/member/update/{id}")
	public Map<String, String> updateUserProfile(@PathVariable Integer id, @RequestBody MemberDTO updatedUser) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        updatedUser.setId(id);
	        Member updatedMember = userUservice.updateMember(updatedUser);
	        if (updatedMember != null) {
	            response.put("success", "更新成功");
	        } else {
	            response.put("error", "更新失敗");
	        }
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
	           response.put("userId", loggedInUser.getId()); // 将用户的ID添加到响应
	           Integer typeId = loggedInUser.getMembertypeid().getId(); // 假设MemberType有一个getId方法来获取ID

	           switch (typeId) {
	               case 1:
	                   response.put("role", "超级管理员");
	                   break;
	               case 2:
	                   response.put("role", "卖家");
	                   break;
	               case 3:
	                   response.put("role", "买家");
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
