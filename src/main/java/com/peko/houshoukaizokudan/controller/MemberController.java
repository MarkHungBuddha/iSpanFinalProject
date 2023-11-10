package com.peko.houshoukaizokudan.controller;

import com.peko.houshoukaizokudan.model.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.MemberDTO;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.service.MemberService;
import com.peko.houshoukaizokudan.service.ProductImageService;
import com.peko.houshoukaizokudan.model.MemberType;


import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Pattern;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
public class MemberController {

	@Autowired
	private MemberService userUservice;
	@Autowired
	private ProductImageService imageUservice;



	@PostMapping("/public/api/member/post")
	public ResponseEntity<Map<String, String>> postRegister(
			@RequestParam("username") String username,
			@RequestParam("passwdbcrypt") String password,
			@RequestParam(value = "firstname", required = false, defaultValue = "小明") String firstname,
			@RequestParam(value = "lastname", required = false, defaultValue = "王") String lastname,
			@RequestParam(value = "gender", required = false, defaultValue = "男") String gender,
			@RequestParam(value = "birthdate", required = false, defaultValue = "1995-07-18") String birthdate,
			@RequestParam(value = "phone", required = false, defaultValue = "0999999999") String phone,
			@RequestParam(value = "membertypeid", required = false, defaultValue = "3") Integer membertypeid,
			@RequestParam("email") String email) {

		Map<String, String> response = new HashMap<>();

		if (!Pattern.matches("^[A-Za-z0-9]{5,}$", username)) {
			response.put("errorMsg", "帳號格式不正確，只能包含英文和数字");
			return ResponseEntity.badRequest().body(response);
		}

		if (!Pattern.matches("^[A-Za-z0-9]{8,}$", password)) {
			response.put("errorMsg", "密碼格式不正確，只能包含英文和数字");
			return ResponseEntity.badRequest().body(response);
		}

		if (!Pattern.matches("^[A-Za-z\u4e00-\u9fa5]+$", firstname) || !Pattern.matches("^[A-Za-z\u4e00-\u9fa5]+$", lastname)) {
			response.put("errorMsg", "名字和姓氏只能使用英文或中文");
			return ResponseEntity.badRequest().body(response);
		}

		if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", birthdate)) {
			response.put("errorMsg", "生日格式不正确，應為YYYY-MM-DD");
			return ResponseEntity.badRequest().body(response);
		}

		if (!Pattern.matches("^09\\d{8}$", phone)) {
			response.put("errorMsg", "手機格式不正確，應為09開頭的10位數字");
			return ResponseEntity.badRequest().body(response);
		}

		// 进行用户名和邮箱的存在性检查
		boolean isExist = userUservice.checkIfUsernameExist(username);
		if (isExist) {
			response.put("errorMsg", "此帳號已存在，請用別的");
			return ResponseEntity.badRequest().body(response);
		}

		boolean isEmailExist = userUservice.checkIfEmailExist(email);
		if (isEmailExist) {
			response.put("errorMsg", "此Email已存在，請用別的");
			return ResponseEntity.badRequest().body(response);
		}

		// 如果所有检查都通过，则创建新用户对象并设置其属性
		Member u1 = new Member();
		MemberType memberType = new MemberType();
		memberType.setId(membertypeid); // 默认会员类型为3
		u1.setMembertypeid(memberType);
		u1.setUsername(username);
		u1.setPasswdbcrypt(password);
		u1.setFirstname(firstname);
		u1.setLastname(lastname);
		u1.setGender(gender);
		u1.setBirthdate(birthdate);
		u1.setPhone(phone);
		u1.setEmail(email);

		// 设置其他默认值
		u1.setCountry("Taiwan");
		u1.setPostalcode("710");
		u1.setCity("台南市");
		u1.setRegion("永康區");
		u1.setStreet("南台街");
		u1.setMemberimgpath("nnNjLVE");

		Instant now = Instant.now();
		String iso8601Time = DateTimeFormatter.ISO_INSTANT.format(now);
		u1.setMembercreationdate(iso8601Time);

		// 将用户添加到数据库
		userUservice.addUser(u1);

		response.put("okMsg", "註冊成功，登入後請編輯個人資料");
		return ResponseEntity.ok(response);
	}



	@PostMapping("/public/api/member/memberLogin")
	public ResponseEntity<?> checkUserLogin(
			@RequestParam("username") String username,
			@RequestParam("passwdbcrypt") String password,
			HttpSession httpsession) {
		// 嘗試登入
		try {
			Member result = userUservice.checkLogin(username, password);

			// 如果用戶存在，驗證密碼是否匹配
			if (result != null) {
				httpsession.setAttribute("loginUser", result);
				// 返回成功登入的信息和用戶資料（視需求而定，可能僅返回部分信息）
				return ResponseEntity.ok().body(Map.of(
						"message", "登入成功",
						"userId", result.getId()
				));
			} else {
				// 如果用戶名不存在或密碼不匹配，返回錯誤信息
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
						"error", "帳號或密碼錯誤"
				));
			}
		} catch (Exception e) {
			// 如果在登入過程中發生異常，返回一般錯誤
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
					"error", "登入過程中發生錯誤：" + e.getMessage()
			));
		}
	}

	@PostMapping("/public/member/logout")
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
			response.put("memberId", loggedInUser.getId()); // 将用户的ID添加到响应
			Integer typeId = loggedInUser.getMembertypeid().getId(); // 假设MemberType有一个getId方法来获取ID

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
	@DeleteMapping("/member/api/member/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable Integer id) {
		try {
			userUservice.deleteMember(id);
			return ResponseEntity.ok("會員已成功刪除");
		} catch (Exception e) {
			// Here you can handle different exceptions differently
			// For example, if a member with the given ID does not exist, you might want to return a 404
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除會員時出錯: " + e.getMessage());
		}
	}
	@GetMapping("/public/api/currentUser")
	public ResponseEntity<?> getCurrentUser(HttpSession session) {
		Member loggedInUser = (Member) session.getAttribute("loginUser");
		if (loggedInUser != null) {
			// 用户已登录，可以获取用户的 ID
			return ResponseEntity.ok(loggedInUser.getId());
		} else {
			// 用户未登录
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入，請先登入");
		}
	}

	@GetMapping("/public/api/userType")
	public Integer getuserType(HttpSession session){
		Member loggedInUser = (Member) session.getAttribute("loginUser");
		return userUservice.finduserType(loggedInUser.getId());
	}
	// 其他控制器方法和功能
}