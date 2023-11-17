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
    private Integer memberID;
    private Set<ProductIDandQuentity> productIDandQuentities; // 一個列表，包含多個商品和相關的資訊
    private BigDecimal totalPrice; // 總價
}
