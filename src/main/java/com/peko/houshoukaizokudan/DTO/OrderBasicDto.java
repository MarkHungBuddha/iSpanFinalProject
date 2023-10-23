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
	private String choosepayment;
	private Integer totalamount;
	private Integer rating;
	private String reviewcontent; //評價
	private String merchantid; // 特店編號 必填
	private String merchanttradenno; // 特店訂單編號 必填  特店訂單編號均為唯一值
	private String paymenttype; // 交易類型 必填aio
	private String tradedesc; // 交易描述 必填 請勿帶入特殊字元
	private String itemname; // 商品名稱 必填 因此建議請固定設定『XX商城商品一批X1』的固定字樣
	private String returnurl; // 付款完成通知回傳網址 必填 付款結果通知回傳網址，為特店server或主機的URL，用來接收綠界後端
	private String checkmacvalue; // 檢查碼 必填 比較複雜 看官網 需URL encode、轉為大小寫、SHA256加密
	private Integer encrypttype; // CheckMacValue加密類型 必填 請固定填入1，使用SHA256加密。
	private String clientbackurl; // Client端返回特店的按鈕連結 消費者點選此按鈕後，會將頁面導回到此設定的網址
	private String itemurl;  // 商品銷售網址
	private String orderresulturl; // Client端回傳付款結果網址 消費者付款完成後，綠界會將付款結果參數以POST方式回傳到到該網址
	private String needextrapaidinfo; // 是否需要額外的付款資訊 不回傳額外的付款資訊時，參數值請傳：Ｎ
	private String statusname; //訂單狀態
	private OrderStatus statusid; //訂單狀態
	private String orderaddess;
}
