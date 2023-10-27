package com.peko.houshoukaizokudan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.DTO.OrderDetailDto;
import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;
import com.peko.houshoukaizokudan.Repository.OrderDetailRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.OrderDetail;
import com.peko.houshoukaizokudan.model.OrderStatus;
import com.peko.houshoukaizokudan.model.ProductBasic;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;
	@Autowired
	private OrderDetailRepository OrderDetailRepo;
	@Autowired
	private ShoppingCartRepository shoppingCartRepo;

	@Transactional
	public List<OrderBasic> findOrderBasicBymemberOrderid(Member buyer) {
		List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);
		if (orders.isEmpty()) {
			return null;
		}
		return orders;
	}

	//修改訂單dto 重複使用
	public OrderBasicDto updateOrderDto(OrderBasic order) {
		OrderBasicDto dto = new OrderBasicDto();
		dto.setOrderid(order.getId());
		dto.setSeller(order.getSeller().getUsername());
		dto.setBuyer(order.getBuyer().getUsername());
		dto.setMerchanttradedate(order.getMerchanttradedate());
		dto.setTotalamount(order.getTotalamount());
		dto.setStatusname(order.getStatusid().getStatusname());
		dto.setOrderaddess(order.getOrderaddress());
		//存商品內容
		Integer orderid = order.getId();
		List<OrderDetail> products = OrderDetailRepo.findOrderDetailByOrderid(orderid);
				
		List<OrderDetailDto> productDtos = products.stream()
	            .map(product -> convertToProductDto(product))
	            .collect(Collectors.toList());
		
		dto.setProducts(productDtos);
		//////
		return dto;
	}
	
	//商品 dto
    private OrderDetailDto convertToProductDto(OrderDetail product) {
    	OrderDetailDto productDto = new OrderDetailDto();
//    	productDto.setImage(product.getProductid().getProductImage());
    	productDto.setProductName(product.getProductid().getProductname());
    	productDto.setQuantity(product.getQuantity());
    	productDto.setUnitprice(product.getUnitprice().intValue());
        return productDto;
    }

	// 買家找訂單 包含商品內容
	@Transactional
	public Page<OrderBasicDto> getOrderAll(Pageable pageable, Member loginUser) {
		Page<OrderBasic> page = orderRepo.findOrderBasicBybuyer(loginUser, pageable);
		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> updateOrderDto(order))
				.collect(Collectors.toList());
		return new PageImpl<>(dtoOrderList, pageable, page.getTotalElements());
	}

	// 取的訂單 by 訂單id
	@Transactional
	public OrderBasic getOrder(Integer orderId) {
		OrderBasic orders = orderRepo.findOrderBasicById(orderId);
		return orders;
	}

	// 買家找訂單dto 舊的XXX
	public List<OrderBasicDto> getOrder(List<OrderBasic> orders) {
		List<OrderBasicDto> dtoOrderList = orders.stream().map(order -> {
			OrderBasicDto dto = updateOrderDto(order);
			return dto;

		}).collect(Collectors.toList()); // 将映射后的对象收集到列表中
		return dtoOrderList;
	}

	// 買家找訂單 改成page
	@Transactional
	public Page<OrderBasicDto> getOrder(Pageable pageable, Member loginUser) {

		Page<OrderBasic> page = orderRepo.findOrderBasicBybuyer(loginUser, pageable);
		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> {
			OrderBasicDto dto = updateOrderDto(order);
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
			OrderBasicDto dto = updateOrderDto(order);
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

	// 修改訂單 變成取消訂單
	@Transactional
	public OrderBasic cancelOrder(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(5);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}

	// 修改訂單 (按付款按鈕) 待付款 > 待出貨
	@Transactional
	public OrderBasic payOrder(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(2);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}

	// 賣家 查訂單 (Page)
	@Transactional
	public Page<OrderBasicDto> getSellerOrder(Pageable pageable, Member loginUser) {
		Page<OrderBasic> page = orderRepo.findOrderBasicByseller(loginUser, pageable);
		List<OrderBasicDto> dtoOrderList = page.getContent().stream().map(order -> {
			OrderBasicDto dto = updateOrderDto(order);
			return dto;

		}).collect(Collectors.toList());
		return new PageImpl<>(dtoOrderList, pageable, page.getTotalElements());
	}

	// 賣家 修改訂單 按出貨按鈕 變成待收貨
	@Transactional
	public OrderBasic shipOrder(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(3);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}
	
	//送出購物車內容
	@Transactional
    public checkoutOrderDto processCheckout(Member member, List<ProductIDandQuentity> productItems) throws Exception {
        System.out.println(productItems.toString());

        List<Integer> productIdsList = productItems.stream()
                .map(ProductIDandQuentity::getProductID)
                .collect(Collectors.toList());


        List<ProductBasic> cartProducts = shoppingCartRepo.findProductsByUserIdAndProductIds(
                member.getId(), productIdsList);
        System.out.println("cartProducts=" + cartProducts.toString());

        if (cartProducts.size() != productItems.size()) {
            throw new Exception("部分商品不存在購物車內");
        }

        List<ProductIDandQuentity> productDtos = new ArrayList<>();

        // 遍历每一个购物车里的产品
        for (ProductBasic product : cartProducts) {

            // 1. 检查请求的商品是否都在用户的购物车内
            if (!productItems.stream().anyMatch(item -> item.getProductID().equals(product.getId()))) {
                throw new Exception("部分商品不存在購物車內");
            }

            // 获取这个产品的数量
            int quantity = 0;
            for (ProductIDandQuentity item : productItems) {
                if (item.getProductID().equals(product.getId())) {
                    quantity = item.getQuantity();
                    break;
                }
            }

            // 2. 检查所有商品是否都属于同一卖家。
            if (!product.getSellermemberid().getId().equals(cartProducts.get(0).getSellermemberid().getId())) {
                throw new Exception("結帳商品需要是同賣家");
            }

            // 3. 检查商品库存。
            if (product.getQuantity() <= 0) {
                throw new Exception("商品: " + product.getProductname() + " 已售完");
            }

            // 确定产品的价格
            BigDecimal effectivePrice;
            if (product.getSpecialprice() != null && product.getSpecialprice().compareTo(BigDecimal.ZERO) > 0) {
                effectivePrice = product.getSpecialprice();
            } else {
                effectivePrice = product.getPrice();
            }

            // 创建一个新的ProductIDandQuentity DTO并添加到列表中
            ProductIDandQuentity dto = new ProductIDandQuentity(product.getId(), quantity, effectivePrice, product.getProductname());
            productDtos.add(dto);
        }


        double totalPrice = productDtos.stream().mapToDouble(dto -> dto.getPrice().doubleValue() * dto.getQuantity()).sum();

        checkoutOrderDto orderDto = new checkoutOrderDto(
                member.getId(),
                new HashSet<>(productDtos),
                BigDecimal.valueOf(totalPrice)
        );

        return orderDto;
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
