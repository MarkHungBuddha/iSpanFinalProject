package com.peko.houshoukaizokudan.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.service.OrderBasicService;

import ecpay.payment.integration.*;
import ecpay.payment.integration.domain.AioCheckOutDevide;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	@Autowired
	private OrderBasicService orderService;

	// 跳頁
	@GetMapping("/orders/orderBase")
	public String orderShowPage() {
		return "order/showOrders";
	}

	@PostMapping("/orders/orderBase")
	public String orderShow(HttpSession session, Model model) {
	    Member loginUser = (Member) session.getAttribute("loginUser");
	    
	    if (loginUser != null) {
	        List<OrderBasic> orders = orderService.findOrderBasicDataBymemberid(loginUser);
	        model.addAttribute("orders", orders);
	        return "order/showOrders";
	    } else {
	        // 如果会话中没有登录用户信息，可以重定向到登录页面或采取其他操作
	        return "order/showOrders";
	    }
	}
	
	/*準備前往綠界*/
	@GetMapping("/goEcPay")
	public void goEcPay(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session)
	        throws IOException {
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
	    aioCheck.setMerchantTradeNo("testSpeakitup" + ob.getId()); // Changed to getOrderno() and fixed case
	    // 付款完成回傳網址
	    aioCheck.setReturnURL("https://speakitup.nctu.me/order/returnURL");
	    // Client端回傳付款結果網址
	    aioCheck.setOrderResultURL("https://speakitup.nctu.me/order/showHistoryOrder");

	    // 輸出 HTML
	    PrintWriter out = response.getWriter(); // Changed to getWriter()
	    response.setContentType("text/html");
	    out.print(aio.aioCheckOut(aioCheck, null));
	}
	
	//綠界回傳資料
	@PostMapping("/returnURL")
	public void returnURL(@RequestParam("MerchantTradeNo") String MerchantTradeNo,
	                      @RequestParam("RtnCode") int RtnCode,
	                      @RequestParam("TradeAmt") int TradeAmt,
	                      HttpServletRequest request) {
	    // 交易成功
	    if ((request.getRemoteAddr().equalsIgnoreCase("175.99.72.1") ||
	         request.getRemoteAddr().equalsIgnoreCase("175.99.72.11") ||
	         request.getRemoteAddr().equalsIgnoreCase("175.99.72.24") ||
	         request.getRemoteAddr().equalsIgnoreCase("175.99.72.28") ||
	         request.getRemoteAddr().equalsIgnoreCase("175.99.72.32")) && RtnCode == 1) {
	        String orderIdStr = MerchantTradeNo.substring(13);
	        int orderId = Integer.parseInt(orderIdStr);
	        OrderBasic ob = orderService.getOrder(orderId);
	        ob.setStatusid(3);
	    }
	    // 在这里你需要处理交易失败的情况，添加相应的逻辑。
	}
	
	//查詢歷史清單
	@PostMapping("/showHistoryOrder")
	public String showECPAYHistoryOrder(Model model, HttpSession session) {
	    // 取得使用者資料(MemberBean)
		Member mb = (Member) session.getAttribute("LoginOK");
	    Integer memberId = mb.getId();
	    
	    // 取得使用者的訂單資料(OrderBean)
	    List<OrderBasic> orders = orderService.getMemberOrders(memberId);
	    
	    // 取出訂單詳細資料(OrderItemBean)
	    Map<Integer, Set<OrderItemBean>> orderItemGroup = new HashMap<Integer, Set<OrderItemBean>>();
	    for (OrderBean ob : orders) {
	        int orderNo = ob.getOrderNo(); // Changed to getOrderNo()
	        Set<OrderItemBean> orderItemBeans = ob.getOrderItems(); // Changed to orderItemBeans
	        orderItemGroup.put(orderNo, orderItemBeans);
	    }
	    
	    model.addAttribute("order_list", orders);
	    model.addAttribute("orderItem_map", orderItemGroup);
	    
	    return "order/historyOrder";
	}



}
