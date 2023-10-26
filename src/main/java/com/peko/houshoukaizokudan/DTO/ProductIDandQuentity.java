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
public class ProductIDandQuentity {
    private Integer productID; // 商品物件
    private int quantity;         // 商品數量

}
