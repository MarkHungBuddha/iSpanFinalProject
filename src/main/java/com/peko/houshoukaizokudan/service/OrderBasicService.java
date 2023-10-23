package com.peko.houshoukaizokudan.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderStatus;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;

	public List<OrderBasic> findOrderBasicBymemberOrderid(Member buyer) {
//		Integer Member= buyer.getId();

		List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);

		if (orders.isEmpty()) {
			return null;
		}

		return orders;
	}

	public OrderBasic getOrder(Integer orderId) {
		OrderBasic orders = orderRepo.findOrderBasicByOrderid(orderId);
		return orders;
	}

	// 找訂單dto
	public List<OrderBasicDto> getOrder(List<OrderBasic> orders) {
		List<OrderBasicDto> dtoOrderList = orders.stream().map(order -> {
			OrderBasicDto dto = new OrderBasicDto();
			dto.setOrderid(order.getOrderid());
			dto.setSeller(order.getSeller().getUsername());
			dto.setBuyer(order.getBuyer().getUsername());
			dto.setMerchanttradedate(order.getMerchanttradedate());
			dto.setChoosepayment(order.getChoosepayment());
			dto.setTotalamount(order.getTotalamount());
			dto.setRating(order.getRating());
			dto.setReviewcontent(order.getReviewcontent());
			dto.setMerchantid(order.getMerchantid());
			dto.setMerchanttradenno(order.getMerchanttradenno());
			dto.setPaymenttype(order.getPaymenttype());
			dto.setTradedesc(order.getTradedesc());
			dto.setItemname(order.getItemname());
			dto.setStatusname(order.getStatusid().getStatusname());
			dto.setOrderaddess(order.getOrderaddress());
			return dto;
		}).collect(Collectors.toList()); // 将映射后的对象收集到列表中
		return dtoOrderList;
	}

	// 儲存新訂單
	public OrderBasic updateOrderBasic(OrderBasic order, OrderBasic updateOrder) {

		order.setOrderaddress(updateOrder.getOrderaddress());
		return orderRepo.save(order);
	}

	// 修改訂單dto
	public OrderBasicDto updateOrderDto(OrderBasic order) {

		OrderBasicDto dto = new OrderBasicDto();
		dto.setOrderid(order.getOrderid());
		dto.setSeller(order.getSeller().getUsername());
		dto.setBuyer(order.getBuyer().getUsername());
		dto.setMerchanttradedate(order.getMerchanttradedate());
		dto.setChoosepayment(order.getChoosepayment());
		dto.setTotalamount(order.getTotalamount());
		dto.setRating(order.getRating());
		dto.setReviewcontent(order.getReviewcontent());
		dto.setMerchantid(order.getMerchantid());
		dto.setMerchanttradenno(order.getMerchanttradenno());
		dto.setPaymenttype(order.getPaymenttype());
		dto.setTradedesc(order.getTradedesc());
		dto.setItemname(order.getItemname());
		dto.setStatusname(order.getStatusid().getStatusname());
		dto.setOrderaddess(order.getOrderaddress());
		return dto;
	}

	// 修改訂單 變成取消訂單
	public OrderBasic cancelOrderBasic(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(5);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}

	// 產生訂單
//	public List<OrderBasic> save(List<ProductBasic> buyProducts, Member loginUser) {
//		OrderBasic orderBasic = new OrderBasic();
//		Integer totalamount = orderBasic.getTotalamount();
//		totalamount=0;
//		for(ProductBasic product :buyProducts) {
//			totalamount=+(int)product.getPrice().intValue();
//		}
//		
//		orderBasic.setTotalamount(totalamount);
//		orderBasic.setSeller(buyProducts.get(0).getSellermemberid());
//		orderBasic.setmemberid();
//		
//		
//		List<OrderBasic>order=orderRepo.saveAll(buyProducts);
//		return order;
//	}

//	public List<OrderBasic> getMemberOrders(Integer memberId) {
//		List<OrderBasic> orders=orderRepo.findOrderBasicBybuyer(memberId);
//		return orders;
//	}

}
