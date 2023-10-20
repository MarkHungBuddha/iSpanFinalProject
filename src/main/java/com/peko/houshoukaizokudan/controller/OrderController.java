package com.peko.houshoukaizokudan.controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderStatus;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.service.OrderBasicService;
import com.peko.houshoukaizokudan.service.OrderStatusService;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutDevide;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {
	@Autowired
	private OrderBasicService orderService;
	@Autowired
	private OrderStatusService orderStatusService;

	// 跳頁
	@GetMapping("/orders/orderBase")
	public String orderShowPage() {
		return "order/showOrders";
	}

	//使用者訂單查詢
	@PostMapping("/orders/orderBase")
	public List<OrderBasicDto> orderShow(HttpSession session) {

		Member loginUser = (Member) session.getAttribute("loginUser");

		if (loginUser != null) {
			
	        List<OrderBasic> orders = orderService.findOrderBasicBymemberOrderid(loginUser);
	        
	        List<OrderBasicDto> dtoOrderList= orderService.getOrder(orders);
	        
	        return dtoOrderList;
	    } else {
	        return null;
	    }
	}
	
	//下單
	@PostMapping("/order/addOrder")
	public List<OrderBasic> placeOrder(HttpSession session){
		
		Member loginUser = (Member) session.getAttribute("loginUser");
		List<ProductBasic> buyProducts = (List<ProductBasic>) session.getAttribute("buyProducts");
		List<OrderBasic>order=orderService.save(buyProducts,loginUser);
		
		
		return order;
	}

	
	
	
	/* 準備前往綠界 */
	 @GetMapping("/goEcPay")
	public void goEcPay(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException  {
		OrderBasic ob = (OrderBasic) request.getAttribute("OrderBasic");

		// 設定金流
		AllInOne aio = new AllInOne("");
		AioCheckOutDevide aioCheck = new AioCheckOutDevide();
		// 特店編號
		aioCheck.setMerchantID("2000214");
		// 特店交易時間
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		sdf.setLenient(false);
		aioCheck.setMerchantTradeDate(sdf.format(new Date()));
		// 交易金額
		aioCheck.setTotalAmount(ob.getTotalamount().toString()); // Changed to String.valueOf()
		// 交易描述
		aioCheck.setTradeDesc("speakitup");
		// 商品名稱
		aioCheck.setItemName("500");
		// 特店交易編號 唯一
		aioCheck.setMerchantTradeNo("testSpeakitup" + ob.getOrderid()); // Changed to getOrderno() and fixed case
		// 付款完成回傳網址
		aioCheck.setReturnURL("https://speakitup.nctu.me/order/returnURL");
		// Client端回傳付款結果網址
		aioCheck.setOrderResultURL("https://speakitup.nctu.me/order/showHistoryOrder");

		// 輸出 HTML
		PrintWriter out = response.getWriter(); // Changed to getWriter()
		response.setContentType("text/html");
		out.print(aio.aioCheckOut(aioCheck, null));
	}

	// 綠界回傳資料
	@PostMapping("/returnURL")
	public void returnURL(
			@RequestParam("MerchantTradeNo") String MerchantTradeNo, // 特店訂單編號
			@RequestParam("RtnCode") int RtnCode, // 交易訊息
			@RequestParam("TradeAmt") int TradeAmt, // 交易金額
			HttpServletRequest request) {
		// 交易成功
		if ((request.getRemoteAddr().equalsIgnoreCase("175.99.72.1")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.11")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.24")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.28")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.32")) && RtnCode == 1) {
			String orderIdStr = MerchantTradeNo.substring(13);
			int orderId = Integer.parseInt(orderIdStr);
			OrderBasic ob = orderService.getOrder(orderId);
			//設定交易狀態
			OrderStatus OrderStatus = orderStatusService.findOrderStatusById(3);
			ob.setStatusid(OrderStatus);
		}
		// 在这里你需要处理交易失败的情况，添加相应的逻辑。
	}

	// 查詢歷史清單
//	 @PostMapping("/showHistoryOrder")
//	public String showECPAYHistoryOrder(Model model, HttpSession session) {
//	    // 取得使用者資料(MemberBean)
//		Member mb = (Member) session.getAttribute("LoginOK");
//	    Integer memberId = mb.getId();
//	    
//	    // 取得使用者的訂單資料(OrderBean)
//	    List<OrderBasic> orders = orderService.getMemberOrders(memberId);
//	    
//	    // 取出訂單詳細資料(OrderItemBean)
//	    Map<Integer, Set<OrderDetail>> orderItemGroup = new HashMap<Integer, Set<OrderDetail>>();
//	    for (OrderDetail ob : orders) {
//	        int orderNo = ob.getId(); // Changed to getOrderNo()
//	        Set<OrderDetail> orderItemBeans = ob.getProductid(); // Changed to orderItemBeans
//	        orderItemGroup.put(orderNo, orderItemBeans);
//	    }
//	    
//	    model.addAttribute("order_list", orders);
//	    model.addAttribute("orderItem_map", orderItemGroup);
//	    
//	    return "order/historyOrder";
//	}

}
