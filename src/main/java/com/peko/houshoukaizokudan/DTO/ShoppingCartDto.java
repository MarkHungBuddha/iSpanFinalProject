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
@Builder
public class ShoppingCartDto {
    private Integer transactionId;
    private Integer ProductId;
    private Integer memberid;
    private BigDecimal price;
    private BigDecimal specialprice;
    private String productname;
    private Integer quantity;
    private String imagepath;
}
