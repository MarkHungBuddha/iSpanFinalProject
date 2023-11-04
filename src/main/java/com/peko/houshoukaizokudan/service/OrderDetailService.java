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
import com.peko.houshoukaizokudan.model.ProductImage;
import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.DTO.OrderDetailDto;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;
import com.peko.houshoukaizokudan.Repository.OrderDetailRepository;
import com.peko.houshoukaizokudan.Repository.ProductImageRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepo;

	@Autowired
	private ProductImageRepository productImageRepo;

	// 找到訂單商品
	@Transactional
	public List<OrderDetail> findOrderDetailByOrderid(Integer orderid) {

		return orderDetailRepo.findOrderDetailByOrderid(orderid);
	}

	
	// 找訂單每個商品 (dto) //增加setProductid 20231104
	@Transactional
	public List<OrderDetailDto> getProducts(List<OrderDetail> products) {
		List<OrderDetailDto> dtoProductList = products.stream().map(product -> {
			OrderDetailDto dto = new OrderDetailDto();
			// 找圖片7碼
			Integer productId = product.getProductid().getId();
			String imagepath = productImageRepo.findProductImagebyproductid(productId);
			dto.setImagepath(imagepath);

			dto.setProductName(product.getProductid().getProductname());
			dto.setQuantity(product.getQuantity());
			dto.setUnitprice(product.getUnitprice().intValue());
			dto.setProductid(productId);		//20231104新增
			Integer detailid = orderDetailRepo.findIdByOrderid_IdAndProductid_Id(product.getOrderid().getId(),productId);
			dto.setOrederDetailid(detailid);	//20231104新增
;
			return dto;
		}).collect(Collectors.toList()); // 将映射后的对象收集到列表中
		return dtoProductList;
	}

}
