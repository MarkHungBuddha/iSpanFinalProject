package com.peko.houshoukaizokudan.model;

import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Nationalized;

import com.peko.houshoukaizokudan.DTO.OrderBasicDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "OrderBasic", schema = "dbo")
public class OrderBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private Integer orderid;

    @ManyToOne
    @JoinColumn(name = "sellerid")
    private Member seller; // 對應到 sellerid

    @ManyToOne
    @JoinColumn(name = "memberid")
    private Member buyer; // 對應到 memberid

    @Nationalized
    @Column(name = "merchanttradedate", length = 20)
    private String merchanttradedate; // 特店交易時間 必填 格式為：yyyy/MM/dd HH:mm:ss

    @Nationalized
    @Column(name = "choosepayment", length = 20)
    private String choosepayment; // 選擇預設付款方式 必填  ALL：不指定付款方式，由綠界顯示付款方式選擇頁面。

    @Column(name = "totalamount")
    private Integer totalamount; // 交易金額 必填 請帶整數，不可有小數點。僅限新台幣

    @Column(name = "rating")
    private Integer rating;

    @Nationalized
    @Column(name = "reviewcontent", length = 400)
    private String reviewcontent; //評價

    @Nationalized
    @Column(name = "merchantid", length = 10)
    private String merchantid; // 特店編號 必填

    @Nationalized
    @Column(name = "merchanttradenno", length = 20)
    private String merchanttradenno; // 特店訂單編號 必填  特店訂單編號均為唯一值

    @Nationalized
    @Column(name = "paymenttype", length = 20)
    private String paymenttype; // 交易類型 必填aio

    @Nationalized
    @Column(name = "tradedesc", length = 200)
    private String tradedesc; // 交易描述 必填 請勿帶入特殊字元

    @Nationalized
    @Column(name = "itemname", length = 400)
    private String itemname; // 商品名稱 必填 因此建議請固定設定『XX商城商品一批X1』的固定字樣

    @Nationalized
    @Column(name = "returnurl", length = 200)
    private String returnurl; // 付款完成通知回傳網址 必填 付款結果通知回傳網址，為特店server或主機的URL，用來接收綠界後端

    @Nationalized
    @Column(name = "checkmacvalue", length = 200)
    private String checkmacvalue; // 檢查碼 必填 比較複雜 看官網 需URL encode、轉為大小寫、SHA256加密

    @Column(name = "encrypttype")
    private Integer encrypttype; // CheckMacValue加密類型 必填 請固定填入1，使用SHA256加密。

    @Nationalized
    @Column(name = "clientbackurl", length = 200)
    private String clientbackurl; // Client端返回特店的按鈕連結 消費者點選此按鈕後，會將頁面導回到此設定的網址

    @Nationalized
    @Column(name = "itemurl", length = 200)
    private String itemurl; // 商品銷售網址

    @Nationalized
    @Column(name = "orderresulturl", length = 200)
    private String orderresulturl; // Client端回傳付款結果網址 消費者付款完成後，綠界會將付款結果參數以POST方式回傳到到該網址

    @Nationalized
    @Column(name = "needextrapaidinfo", length = 1)
    private String needextrapaidinfo; // 是否需要額外的付款資訊 不回傳額外的付款資訊時，參數值請傳：Ｎ
    
    @Nationalized
    @Column(name = "orderaddress",length = 200) // 訂單地址
    private String orderaddress;

    @OneToMany(mappedBy = "orderid")
    private Set<OrderDetail>  orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    @Fetch(FetchMode.JOIN)
    private OrderStatus statusid;





}