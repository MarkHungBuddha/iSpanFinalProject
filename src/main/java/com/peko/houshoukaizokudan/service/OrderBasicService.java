package com.peko.houshoukaizokudan.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.peko.houshoukaizokudan.DTO.ProductIDandQuentity;
import com.peko.houshoukaizokudan.DTO.ProductItem;
import com.peko.houshoukaizokudan.DTO.checkoutOrderDto;
import com.peko.houshoukaizokudan.Repository.ProductBasicRepository;
import com.peko.houshoukaizokudan.Repository.ProductReviewRepository;
import com.peko.houshoukaizokudan.Repository.ShoppingCartRepository;
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

    public List<OrderBasic> findOrderBasicDataBymemberid(Member buyer) {
//		Integer Member= buyer.getId();

        List<OrderBasic> orders = orderRepo.findOrderBasicBybuyer(buyer);

        if (orders.isEmpty()) {
            return null;
        }

        return orders;
    }

    public checkoutOrderDto processCheckout(Member member, List<ProductIDandQuentity> productItems) throws Exception {
        System.out.println(productItems.toString());

        // 1. 將 productItems 轉換為 Stream
        Stream<ProductIDandQuentity> productItemStream = productItems.stream();

        // 2. 使用 map 取得每個 ProductBasic 的 ID
        Stream<Integer> productIdsStream = productItemStream.map(item -> item.getProductID());

        // 3. 將 Stream 轉換為 List
        List<Integer> productIdsList = productIdsStream.collect(Collectors.toList());


        System.out.println("!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!shoppingCartRepo.findProductsByUserIdAndProductIds");

        // 1. Check if the products are in the user's cart.
        List<ProductBasic> cartProducts = shoppingCartRepo.findProductsByUserIdAndProductIds(
                member.getId(), productIdsList);
        System.out.println("cartProducts=" + cartProducts.toString());

        // Ensure the products from the request are all in the cart.
        if (cartProducts.size() != productItems.size()) {
            throw new Exception("部分商品不存在購物車內");
        }
        System.out.println("// 1. Check if the products are in the user's cart.");
        // 2. Check if all products belong to the same seller.
        Integer sellerId = cartProducts.get(0).getSellermemberid().getId();
        for (ProductBasic product : cartProducts) {
            if (!product.getSellermemberid().getId().equals(sellerId)) {
                throw new Exception("結帳商品需要是同賣家");
            }
        }

        // 3. Check product stock.
        for (ProductBasic product : cartProducts) {
            if (product.getQuantity() <= 0) {
                throw new Exception("商品: " + product.getProductname() + " 已售完");
            }
        }

        // 4. Calculate the total price.
        double totalPrice = 0.0;
        for (ProductBasic product : cartProducts) {
            BigDecimal effectivePrice = (product.getSpecialprice() != null && product.getSpecialprice().compareTo(BigDecimal.ZERO) > 0) ? product.getSpecialprice() : product.getPrice();
            totalPrice += effectivePrice.doubleValue();
        }

        // Steps 5 & 6: Setup checkoutOrderDto
        checkoutOrderDto orderDto = checkoutOrderDto.builder()
                .member(member)
//                .productItems(new HashSet<>(productItems))// converting list to set
                .totalPrice(BigDecimal.valueOf(totalPrice))
                .build();

        return orderDto;
    }


}
