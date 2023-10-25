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
public class ProductCategoryDto {
	//以搜尋類別(categoryid)id
	
	//ProductBasic 欄位
	private Integer categoryid;
	private String productname;
	private BigDecimal price;
	private BigDecimal specialprice;
	//ProductCategory 欄位
	private String categoryname;
	private Integer parentid;
	//ProductBasic 欄位
	private String parentname;
	//ProductImage 欄位
	private String imagepath;
	
}
