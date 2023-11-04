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
public class OrderDetailDto {
	
	private String imagepath;	//商品圖片 7碼
	private String productName;	//商品名稱
	private Integer quantity;  	//買的商品數量
	private Integer unitprice; 	//單價
	private Integer productid;  //商品ID 20231104新增
	private Integer orederDetailid; //增加訂單明細ID  20231104新增
		
}
