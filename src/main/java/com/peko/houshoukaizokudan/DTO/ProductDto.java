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
	// 模糊搜尋產品名稱
	
	private Integer productid;
//	private Integer sellermemberid;
	private String productname;
	private BigDecimal price;
	private BigDecimal specialprice;
//	private Integer categoryid;
	private String categoryname;
	private Integer quantity;
	private String description;
	private String imagepath; // ProductImage 資料表的imagepath 欄位
	
}
