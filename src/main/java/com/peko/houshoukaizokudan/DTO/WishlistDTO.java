package com.peko.houshoukaizokudan.DTO;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class WishlistDTO {
    private Integer id;
    private Integer memberid;
    private Integer productid;
    private String productname;
    private String productimage;
    private BigDecimal price;
    private BigDecimal specialprice;


}
