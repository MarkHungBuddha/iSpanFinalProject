package com.peko.houshoukaizokudan.DTO;

import com.peko.houshoukaizokudan.model.ProductBasic;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductItem {
    private ProductBasic product; // 商品物件
    private int quantity;         // 商品數量
    private double averageReview; // 商品平均評價
}
