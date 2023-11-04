package com.peko.houshoukaizokudan.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.peko.houshoukaizokudan.DTO.OrderBasicDto;
import com.peko.houshoukaizokudan.DTO.OrderDetailDto;
import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.Repository.*;
import com.peko.houshoukaizokudan.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBasicService {

	@Autowired
	private OrderBasicRepository orderRepo;

	@Autowired
	private ProductBasicRepository productRepo;

	@Autowired
	private ShoppingCartRepository shoppingCartRepo;

	@Autowired
	private OrderDetailRepository orderDetailRepo;

	@Autowired
	private ProductImageRepository productImageRepo;
	@Autowired
	private ProductBasicRepository productBasicRepo;
	@Autowired
	private MemberRepository memberRepo;
	@Autowired
	private OrderStatusRepository orderStatusRepo;

	@Transactional
	public List<OrderBasic> findOrderBasicDataBymemberid(Member buyer) {
//		Integer Member= buyer.getId();

		List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);

		if (orders.isEmpty()) {
			return null;
		}

		return orders;
	}

	@Transactional
	public checkoutOrderDto processCheckout(Member member, List<ProductIDandQuentity> productItems) throws Exception {
		System.out.println(productItems.toString());

		List<Integer> productIdsList = productItems.stream().map(ProductIDandQuentity::getProductID)
				.collect(Collectors.toList());

		List<ProductBasic> cartProducts = shoppingCartRepo.findProductsByUserIdAndProductIds(member.getId(),
				productIdsList);
		System.out.println("cartProducts=" + cartProducts.toString());

		if (cartProducts.size() != productItems.size()) {
			throw new Exception("部分商品不存在購物車內");
		}

		List<ProductIDandQuentity> productDtos = new ArrayList<>();

// 遍历每一个购物车里的产品
		for (ProductBasic product : cartProducts) {

			// 1. 检查请求的商品是否都在用户的购物车内
			if (!productItems.stream().anyMatch(item -> item.getProductID().equals(product.getId()))) {
				throw new Exception("部分商品不存在购物車內");
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
			ProductIDandQuentity dto = new ProductIDandQuentity(product.getId(), quantity, effectivePrice,
					product.getProductname());
			productDtos.add(dto);
		}

		double totalPrice = productDtos.stream().mapToDouble(dto -> dto.getPrice().doubleValue() * dto.getQuantity())
				.sum();

		checkoutOrderDto orderDto = new checkoutOrderDto(member.getId(), new HashSet<>(productDtos),
				BigDecimal.valueOf(totalPrice));

		return orderDto;
	}

	public Integer findTotalByYear(Integer memberId, Integer year) {
		String yearAsString = String.valueOf(year);
		System.out.println(yearAsString);
		System.out.println(memberId);
		return orderRepo.findTotalAmountByYearAndSeller(yearAsString, memberId);
	}

	public Integer findTotalByMonth(Integer year, Integer month, Integer memberIdd) {
    	String yearAsString = String.valueOf(year);
    	System.out.println(yearAsString);
    	String monthAsString = String.valueOf(month);
    	System.out.println(monthAsString);
		return orderRepo.findTotalAmountByYearAndMonthAndSeller(year, month, memberIdd);
	}

	// 修改訂單dto (重複使用)
	public OrderBasicDto updateOrderDto(OrderBasic order) {
		OrderBasicDto dto = new OrderBasicDto();
		dto.setOrderid(order.getId());
		dto.setSeller(order.getSeller().getUsername());
		dto.setBuyer(order.getBuyer().getUsername());
		dto.setMerchanttradedate(order.getMerchanttradedate());
		dto.setTotalamount(order.getTotalamount());
		dto.setStatusname(order.getStatusid().getStatusname());
		dto.setOrderaddess(order.getOrderaddress());
		// 存商品內容
		Integer orderid = order.getId();
		List<OrderDetail> products = orderDetailRepo.findOrderDetailByOrderid(orderid);

		List<OrderDetailDto> productDtos = products.stream().map(product -> convertToProductDto(product))
				.collect(Collectors.toList());

		dto.setProducts(productDtos);
		//////
		return dto;
	}

	@Transactional
	public List<OrderBasic> findOrderBasicBymemberOrderid(Member buyer) {
		List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);
		if (orders.isEmpty()) {
			return null;
		}
		return orders;
	}

	// 商品 dto (重複使用)
	private OrderDetailDto convertToProductDto(OrderDetail product) {
		OrderDetailDto productDto = new OrderDetailDto();

		// 找圖片7碼
		Integer productId = product.getProductid().getId();
		String imagepath = productImageRepo.findProductImagebyproductid(productId);
		productDto.setImagepath(imagepath);

		productDto.setProductName(product.getProductid().getProductname());
		productDto.setQuantity(product.getQuantity()); // 買的商品數量
		productDto.setUnitprice(product.getUnitprice().intValue()); // 單價
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

	// 修改訂單 (按付款按鈕) 待付款(1) > 待出貨(2)
	@Transactional
	public OrderBasic payOrder(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(2);

		order.setStatusid(orderStatus);
		return orderRepo.save(order);
	}

	// 修改訂單 (按付款按鈕) 待收貨(3) > 已完成(4)
	@Transactional
	public OrderBasic completeOrder(OrderBasic order) {
		OrderStatus orderStatus = new OrderStatus();
		orderStatus.setId(4);

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

	// 產生訂單
	@Transactional
	public OrderBasicDto placeOrder(Member loginUser, checkoutOrderDto orderDto, String orderAddress) throws Exception {

		// 庫存檢查
		for (ProductIDandQuentity productInfo : orderDto.getProductIDandQuentities()) {
			int productId = productInfo.getProductID();
			int purchaseQuantity = productInfo.getQuantity();
			ProductBasic product = productBasicRepo.findProductById(productId);
			int stockQuantity = productBasicRepo.findProductByProductid(product.getId());

			if (stockQuantity < purchaseQuantity) {
				// 庫存不足，拋出異常
				throw new Exception("商品 " + product.getProductname() + " 的庫存不足");
			}
		}

		/**** 寫入的訂單資料 ****/
		OrderBasic orderBasic = new OrderBasic();

		Set<ProductIDandQuentity> productIDandQuentities = orderDto.getProductIDandQuentities();
		ProductIDandQuentity firstProduct = productIDandQuentities.iterator().next(); // 取得第一筆商品
		Integer productID = firstProduct.getProductID();
		Integer sellerId = productBasicRepo.findProductBasicSellerIdByproductId(productID);
		Member seller = memberRepo.findmemberBymemberid(sellerId); // 取的商品銷售員資料

		orderBasic.setSeller(seller);
		orderBasic.setBuyer(loginUser);

		// 設定訂單日期時間
		Instant now = Instant.now();

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss")
				.toFormatter().withZone(ZoneId.systemDefault()); // 時區 系統預設

		String iso8601Time = formatter.format(now);
		orderBasic.setMerchanttradedate(iso8601Time);

		BigDecimal totalPrice = orderDto.getTotalPrice();
		orderBasic.setTotalamount(totalPrice.intValue());
		// 設定訂單地址
		orderBasic.setOrderaddress(orderAddress);

		int statusId = 1; // 設定訂單狀態為1(待付款)
		OrderStatus orderStatus = orderStatusRepo.findById(statusId).orElse(null);
		orderBasic.setStatusid(orderStatus);

		orderRepo.save(orderBasic);

		/***** 寫入的商品資料 *****/

		if (orderDto.getProductIDandQuentities() != null && !orderDto.getProductIDandQuentities().isEmpty()) {
			for (ProductIDandQuentity productInfo : orderDto.getProductIDandQuentities()) {
				OrderDetail orderDetail = new OrderDetail();

				int productId = productInfo.getProductID();
				ProductBasic product = productBasicRepo.findProductById(productId);
				orderDetail.setProductid(product);
				orderDetail.setQuantity(productInfo.getQuantity());
				orderDetail.setUnitprice(productInfo.getPrice());

				orderDetail.setOrderid(orderBasic); // 設定訂單關聯

				orderDetailRepo.save(orderDetail);// 保存訂單商品資料
			}
		}

		// 存入dto
		OrderBasicDto orderBasicDto = updateOrderDto(orderBasic);

		/***** 清除已購買的購物車商品&庫存 *****/

		// 購物車內的商品列表
		Set<ProductIDandQuentity> productSet = orderDto.getProductIDandQuentities();
		List<ProductIDandQuentity> productItems = new ArrayList<>(productSet);

		// 取的購買商品ID 清單
		List<Integer> productIdsList = productItems.stream().map(ProductIDandQuentity::getProductID)
				.collect(Collectors.toList());
		// 取得購買的商品 清單
		List<ProductBasic> cartProducts = shoppingCartRepo.findProductsByUserIdAndProductIds(loginUser.getId(),
				productIdsList);

		for (ProductBasic product : cartProducts) {

			// 取得商品庫存數量
			int stockQuantity = productBasicRepo.findProductByProductid(product.getId());

			// 取得購物車商品的數量
			int carQuantity = product.getQuantity();

			// 取得購買商品數量
			Integer purchaseQuantity = orderDetailRepo.getProductQuantityFromorderDetail(orderBasicDto.getOrderid(),
					product.getId());

			if (stockQuantity < purchaseQuantity) {
				// 庫存不足，可以拋出異常訊息
				throw new Exception("商品 " + product.getProductname() + " 的庫存不足");
			}

			// 庫存充足，更新庫存
			stockQuantity -= purchaseQuantity;
			carQuantity -= purchaseQuantity; // 更新購物車數量
			product.setQuantity(carQuantity); // 更新購物車商品數量

			productBasicRepo.updateProductQuantity(product.getId(), stockQuantity); // 更新庫存數量

			// 如果庫存<=0，購物車移除該商品
			if (stockQuantity <= 0 || carQuantity <= 0) {
				shoppingCartRepo.removeProductFromShoppingCart(product.getId(), loginUser.getId());
			} else {
				shoppingCartRepo.saveProductFromShoppingCart(product.getId(), loginUser.getId(), carQuantity);
			}

			//
			productBasicRepo.save(product);
		}

		return orderBasicDto;

	}

}
