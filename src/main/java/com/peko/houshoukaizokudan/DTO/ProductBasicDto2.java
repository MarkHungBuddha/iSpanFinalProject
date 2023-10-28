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
public class ProductBasicDto2 {
    private Integer ProductId;
    private String productName;
    private BigDecimal price;
    private BigDecimal specialPrice;
    private String description;
    private Integer quantity;
    private String categoryName;
    private String parentCategoryName;
//    private Integer sellermemberid;
//    private String imagepath;
}
