package com.peko.houshoukaizokudan.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderDetail;
import com.peko.houshoukaizokudan.model.OrderStatus;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;

	@Transactional
	public List<OrderBasic> findOrderBasicBymemberOrderid(Member buyer) {
//		Integer Member= buyer.getId();

		List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);

		if (orders.isEmpty()) {
			return null;
		}

		return orders;
	}
	//取的訂單 by 訂單id
	@Transactional
	public OrderBasic getOrder(Integer orderId) {
		OrderBasic orders = orderRepo.findOrderBasicById(orderId);
		return orders;
	}

	// 買家找訂單dto 舊的XXX
	public List<OrderBasicDto> getOrder(List<OrderBasic> orders) {
		List<OrderBasicDto> dtoOrderList = orders.stream().map(order -> {
			OrderBasicDto dto = new OrderBasicDto();
			dto.setOrderid(order.getId());
			dto.setSeller(order.getSeller().getUsername());
			dto.setBuyer(order.getBuyer().getUsername());
			dto.setMerchanttradedate(order.getMerchanttradedate());
			dto.setTotalamount(order.getTotalamount());
			dto.setStatusname(order.getStatusid().getStatusname());
			dto.setOrderaddess(order.getOrderaddress());
			return dto;

		}).collect(Collectors.toList()); // 将映射后的对象收集到列表中
		return dtoOrderList;
	}

	// 買家找訂單 改成page
	@Transactional
	public Page<OrderBasicDto> getOrder(Pageable pageable, Member loginUser) {

		Page<OrderBasic> page = orderRepo.findOrderBasicBybuyer(loginUser, pageable);
		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> {
			OrderBasicDto dto = new OrderBasicDto();
			dto.setOrderid(order.getId());
			dto.setSeller(order.getSeller().getUsername());
			dto.setBuyer(order.getBuyer().getUsername());
			dto.setMerchanttradedate(order.getMerchanttradedate());
			dto.setTotalamount(order.getTotalamount());
			dto.setStatusname(order.getStatusid().getStatusname());
			dto.setOrderaddess(order.getOrderaddress());
			return dto;


		}).collect(Collectors.toList());
		return new PageImpl<>(dtoOrderList, pageable, page.getTotalElements());
	}

	// 買家找訂單狀態 改成page
	@Transactional
	public Page<OrderBasicDto> getOrderByStatus(Pageable pageable, Member loginUser, Integer statusid) {

		Integer buyer = loginUser.getId();
		Page<OrderBasic> page = orderRepo.findOrderBasicByStatus(buyer, statusid, pageable);

		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> {
			OrderBasicDto dto = new OrderBasicDto();
			dto.setOrderid(order.getId());
			dto.setSeller(order.getSeller().getUsername());
			dto.setBuyer(order.getBuyer().getUsername());
			dto.setMerchanttradedate(order.getMerchanttradedate());
			dto.setTotalamount(order.getTotalamount());
			dto.setStatusname(order.getStatusid().getStatusname());
			dto.setOrderaddess(order.getOrderaddress());
			return dto;


		}).collect(Collectors.toList());
		return new PageImpl<>(dtoOrderList, pageable, page.getTotalElements());
	}

	// 儲存新訂單
	@Transactional
	public OrderBasic updateOrderBasic(OrderBasic order, OrderBasic updateOrder) {

		order.setOrderaddress(updateOrder.getOrderaddress());
		return orderRepo.save(order);
	}

	// 修改訂單dto
	@Transactional
	public OrderBasicDto updateOrderDto(OrderBasic order) {

		OrderBasicDto dto = new OrderBasicDto();
		dto.setOrderid(order.getId());
		dto.setSeller(order.getSeller().getUsername());
		dto.setBuyer(order.getBuyer().getUsername());
		dto.setMerchanttradedate(order.getMerchanttradedate());
		dto.setTotalamount(order.getTotalamount());
		dto.setStatusname(order.getStatusid().getStatusname());
		dto.setOrderaddess(order.getOrderaddress());
		return dto;

	}

	// 修改訂單 變成取消訂單
	@Transactional
	public OrderBasic cancelOrderBasic(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(5);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}
	
	//賣家 查訂單 (Page)
	public Page<OrderBasicDto> getSellerOrder(Pageable pageable, Member loginUser) {
		Page<OrderBasic> page = orderRepo.findOrderBasicByseller(loginUser, pageable);
		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> {
			OrderBasicDto dto = new OrderBasicDto();
			dto.setOrderid(order.getId());
			dto.setSeller(order.getSeller().getUsername());
			dto.setBuyer(order.getBuyer().getUsername());
			dto.setMerchanttradedate(order.getMerchanttradedate());
			dto.setTotalamount(order.getTotalamount());
			dto.setStatusname(order.getStatusid().getStatusname());
			dto.setOrderaddess(order.getOrderaddress());
			return dto;

		}).collect(Collectors.toList());
		return new PageImpl<>(dtoOrderList, pageable, page.getTotalElements());
	}

	// 產生訂單
//	@Transactional
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
