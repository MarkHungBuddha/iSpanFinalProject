package com.peko.houshoukaizokudan.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.DTO.OrderDetailDto;
import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.model.*;
import com.peko.houshoukaizokudan.service.OrderDetailService;
import com.peko.houshoukaizokudan.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.peko.houshoukaizokudan.service.OrderBasicService;

import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {
	@Autowired
	private OrderBasicService orderService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private OrderDetailService orderDetailService;


//	@PostMapping("/orders/orderBase")
//	public List<OrderBasicDto> orderShow(HttpSession session) {
//		Member loginUser = (Member) session.getAttribute("loginUser");
//
//		if (loginUser != null) {
//
//			List<OrderBasic> orders = orderService.findOrderBasicBymemberOrderid(loginUser);
//
//			List<OrderBasicDto> dtoOrderList = orderService.getOrder(orders);
//
//			return dtoOrderList;
//		} else {
//			return null;
//		}
//	}




	// 會員 購物車送出訂單
	@PostMapping("/customer/api/order/checkout")
	public ResponseEntity<checkoutOrderDto> checkout(@RequestBody List<ProductIDandQuentity> productItems, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser"); // assuming you stored user ID in session
		if(loginUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		System.out.println(productItems.toString());
		try {
			checkoutOrderDto orderDto = orderService.processCheckout(loginUser, productItems);
			return ResponseEntity.ok(orderDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	// 買家找訂單 (page) 不含商品內容
	@GetMapping("/customer/api/findorders")
	public ResponseEntity<?> orderShow(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
									   HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {

			Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "id"); // 每頁10筆訂單
			Page<OrderBasicDto> page = orderService.getOrder(pageable, loginUser);

			return new ResponseEntity<>(page, null, HttpStatus.OK);

		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}

	// 買家找訂單 (page) 有含商品內容
	@GetMapping("/customer/api/findAllOrders")
	public ResponseEntity<?> orderShowAll(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
										  HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {

			Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "id"); // 每頁10筆訂單
			Page<OrderBasicDto> page = orderService.getOrderAll(pageable, loginUser);

			return new ResponseEntity<>(page, null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}

	// 買家找訂單 by 訂單狀態 (page)
	@GetMapping("/customer/api/findorders/Status")
	public ResponseEntity<?> orderShowByStatus(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
											   HttpSession session, @RequestParam Integer statusid) {

		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {

			Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "orderid"); // 每頁10筆訂單
			Page<OrderBasicDto> page = orderService.getOrderByStatus(pageable, loginUser, statusid);
			if (page.isEmpty())
				return new ResponseEntity<>("沒有資料", null, HttpStatus.OK);

			return new ResponseEntity<>(page, null, HttpStatus.OK);

		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}

	// 買家修改訂單地址
	@PutMapping("/customer/api/order/{id}")
	public ResponseEntity<?> changeOrderAddres(@PathVariable Integer id, @RequestBody OrderBasic updateorderaddress,
											   HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		OrderBasic optional = orderService.getOrder(id);

		if (optional == null)
			return new ResponseEntity<>("沒有這筆資料", null, HttpStatus.NOT_FOUND);
		// 確認訂單人且訂單狀態碼是 待付款(1) 待出貨(2) 才可以改變地址
		if (loginUser.getId() == optional.getBuyer().getId() && optional.getStatusid().getId() == 1
				|| optional.getStatusid().getId() == 2) {

			OrderBasic result = orderService.updateOrderBasic(optional, updateorderaddress);
			OrderBasicDto updateOrder = orderService.updateOrderDto(result);

			return new ResponseEntity<>(updateOrder, null, HttpStatus.OK);

		} else
			return new ResponseEntity<>("無法修改地址，訂單狀態碼錯誤或者不是你的訂單", null, HttpStatus.BAD_REQUEST);
	}

	// 買家取消訂單 待付款or待出貨 > 取消
	@PutMapping("/customer/api/{id}/cancelOrder")
	public ResponseEntity<?> cancelOrder(@PathVariable Integer id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		OrderBasic optional = orderService.getOrder(id);

		if (optional == null)
			return new ResponseEntity<>("沒有這筆資料", null, HttpStatus.NOT_FOUND);
		// 確認訂單人且訂單狀態碼是 待付款 待出貨 才可以取消訂單
		if (loginUser.getId() == optional.getBuyer().getId() && optional.getStatusid().getId() == 1
				|| optional.getStatusid().getId() == 2) {

			OrderBasic result = orderService.cancelOrder(optional);
			OrderBasicDto updateOrder = orderService.updateOrderDto(result);

			return new ResponseEntity<>(updateOrder, null, HttpStatus.OK);

		} else
			return new ResponseEntity<>("無法取消訂單，訂單狀態碼錯誤或者不是你的訂單", null, HttpStatus.BAD_REQUEST);
	}

	// 買家訂單按付款按鈕 待付款(1) > 待出貨(2)
	@PutMapping("/customer/api/{id}/payOrder")
	public ResponseEntity<?> payOrder(@PathVariable Integer id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		OrderBasic optional = orderService.getOrder(id);

		if (optional == null)
			return new ResponseEntity<>("沒有這筆資料", null, HttpStatus.NOT_FOUND);
		// 確認訂單人且訂單狀態碼是 待付款(1) > 待出貨(2)
		if (loginUser.getId() == optional.getBuyer().getId() && optional.getStatusid().getId() == 1) {

			OrderBasic result = orderService.payOrder(optional);
			OrderBasicDto updateOrder = orderService.updateOrderDto(result);

			return new ResponseEntity<>(updateOrder, null, HttpStatus.OK);

		} else
			return new ResponseEntity<>("訂單狀態碼錯誤或者不是你的訂單", null, HttpStatus.BAD_REQUEST);
	}

	// 買家訂單按付款按鈕 待收貨(3) > 已完成(4)
	@PutMapping("/customer/api/{id}/completeOrder")
	public ResponseEntity<?> completeOrder(@PathVariable Integer id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		OrderBasic optional = orderService.getOrder(id);

		if (optional == null)
			return new ResponseEntity<>("沒有這筆資料", null, HttpStatus.NOT_FOUND);
		// 確認訂單人且訂單狀態碼是 待收貨(3) > 已完成(4)
		if (loginUser.getId() == optional.getBuyer().getId() && optional.getStatusid().getId() == 3) {

			OrderBasic result = orderService.completeOrder(optional);
			OrderBasicDto updateOrder = orderService.updateOrderDto(result);

			return new ResponseEntity<>(updateOrder, null, HttpStatus.OK);

		} else
			return new ResponseEntity<>("訂單狀態碼錯誤或者不是你的訂單", null, HttpStatus.BAD_REQUEST);
	}

	// 查詢訂單內容的商品
	@GetMapping("/customer/api/orders/orderDetail")
	public ResponseEntity<?> getOrderDetail(@RequestParam Integer orderid, HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {
			// 取得訂單每個商品
			List<OrderDetail> products = orderDetailService.findOrderDetailByOrderid(orderid);
			List<OrderDetailDto> dtoProductList = orderDetailService.getProducts(products);

			return new ResponseEntity<>(dtoProductList, null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}

	// 賣家查訂單
	@GetMapping("/seller/api/findSellerOrders")
	public ResponseEntity<?> sellerOrderShow(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
											 HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {

			Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "id"); // 每頁10筆訂單
			Page<OrderBasicDto> page = orderService.getSellerOrder(pageable, loginUser);

			return new ResponseEntity<>(page, null, HttpStatus.OK);

		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}

	// 賣家訂單按出貨按紐 待出貨(2) > 待收貨(3)
	@PutMapping("/seller/api/{id}/shipOrder")
	public ResponseEntity<?> shipOrder(@PathVariable Integer id, HttpSession session) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		OrderBasic optional = orderService.getOrder(id);

		if (optional == null)
			return new ResponseEntity<>("沒有這筆資料", null, HttpStatus.NOT_FOUND);
		// 確認訂單人且訂單狀態碼是 待出貨 > 待收貨
		if (loginUser.getId() == optional.getSeller().getId() && optional.getStatusid().getId() == 2) {

			OrderBasic result = orderService.shipOrder(optional);
			OrderBasicDto updateOrder = orderService.updateOrderDto(result);

			return new ResponseEntity<>(updateOrder, null, HttpStatus.OK);

		} else
			return new ResponseEntity<>("訂單狀態碼錯誤或者不是你的訂單", null, HttpStatus.BAD_REQUEST);
	}



	// 新增訂單
	@PostMapping("/customer/api/order/addOrder")
	public ResponseEntity<OrderBasicDto> placeOrder(HttpSession session, @RequestBody checkoutOrderDto orderDto,
													@RequestParam String orderAddress) throws Exception {

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {

			OrderBasicDto order = orderService.placeOrder(loginUser, orderDto, orderAddress);

			return new ResponseEntity<>(order, null, HttpStatus.OK);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

	}
	
	//20231103 新增 by昱霖	
	// 找一筆訂單 (page) 有含商品內容
	@GetMapping("/customer/api/findOneOrder")
	public ResponseEntity<?> oneorderShow(@RequestParam Integer orderid,
										  HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser != null) {

			OrderBasicDto order = orderService.getOneOrder(orderid,loginUser);

			return new ResponseEntity<>(order, null, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("使用者未授權", null, HttpStatus.UNAUTHORIZED);
		}
	}
	
	


}
