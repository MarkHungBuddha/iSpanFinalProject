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
public class OrderBasicDto {
	private Integer orderid;
	private String seller;
	private String buyer;
	private String merchanttradedate;
	private Integer totalamount;
	private String statusname; //訂單狀態
	private String orderaddess;
	private Integer statusid;
	private List<OrderDetailDto> products; //訂單商品內容
}
