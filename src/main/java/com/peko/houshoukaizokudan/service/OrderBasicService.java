package com.peko.houshoukaizokudan.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.OrderBasic;
import com.peko.houshoukaizokudan.model.ProductBasic;
import com.peko.houshoukaizokudan.Repository.OrderBasicRepository;

@Service
public class OrderBasicService {

    @Autowired
    private OrderBasicRepository orderRepo;

    @Autowired
    private ProductBasicRepository productRepo;

    @Autowired
    private ShoppingCartRepository shoppingCartRepo;


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

    public Integer findTotalByYear(Integer memberIdd, Integer year) {
        Integer obb=orderRepo.findTotalAmountByYearAndSeller(memberIdd, year);

        return obb;
    }

    public Integer findTotalByMonth(Integer memberIdd, Integer month, Integer year) {
        Integer obb=orderRepo.findTotalAmountByYearAndMonthAndSeller(year,memberIdd, month);

        return obb;
    }
}




