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
	//以搜尋categoryname(分類名稱) 取得categoryid(類別ID)
	
	//ProductBasic 表
	private Integer categoryid;
	private String productname;
	private BigDecimal price;
	private BigDecimal specialprice;
	//ProductCategory 表
	private String categoryname;
	private Integer parentid;
	//ProductBasic 表
	private String parentname;
	//ProductImage 表
	private String imagepath;
	
}
