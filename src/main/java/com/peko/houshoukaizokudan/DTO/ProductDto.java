package com.peko.houshoukaizokudan.DTO;

import java.math.BigDecimal;
import java.util.Set;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductCategory;
import com.peko.houshoukaizokudan.model.ProductImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	private Integer productId;
	private Integer sellermemberid;
	private String productName;
	private BigDecimal price;
	private BigDecimal specialPrice;
	private Integer categoryId;
	private String categoryName;
	private Integer quantity;
	private String description;
	private String imagepath; // ProductImage 資料表的imagepath 欄位
	
}
