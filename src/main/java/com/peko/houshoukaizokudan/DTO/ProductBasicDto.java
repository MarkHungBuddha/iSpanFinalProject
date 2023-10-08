package com.peko.houshoukaizokudan.DTO;

import com.peko.houshoukaizokudan.model.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.peko.houshoukaizokudan.model.ProductBasic}
 */
@Value
@Setter
@Getter
@Builder
@ToString
public class ProductBasicDto implements Serializable {
    Integer id;
    Member sellermemberid;
    String productname;
    BigDecimal price;
    BigDecimal specialprice;
    ProductCategory categoryid;
    ParentCategory parentCategory;
    Integer quantity;
    String description;
    List<ProductImage> productImage;
    List<ProductReview> productReview;
    List<QandA> qandA;


}