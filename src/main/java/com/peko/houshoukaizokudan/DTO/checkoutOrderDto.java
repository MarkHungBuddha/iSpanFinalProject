package com.peko.houshoukaizokudan.DTO;

import com.peko.houshoukaizokudan.model.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class checkoutOrderDto {


    private Member member;        // 會員物件
    private Set<ProductItem> productItems; // 一個列表，包含多個商品和相關的資訊
    private BigDecimal totalPrice; // 總價

//    public double getTotalPrice() {
//        return productItems.stream()
//                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
//                .sum();
//    }
}
