package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sellerid")
    private Member c; // 對應到 sellerid

    @ManyToOne
    @JoinColumn(name = "memberid")
    private Member buyer; // 對應到 memberid

    @Nationalized
    @Column(name = "merchanttradedate", length = 20)
    private String merchanttradedate;

    @Nationalized
    @Column(name = "choosepayment", length = 20)
    private String choosepayment;

    @Column(name = "totalamount")
    private Integer totalamount;

    @Column(name = "rating")
    private Integer rating;

    @Nationalized
    @Column(name = "reviewcontent", length = 400)
    private String reviewcontent;

    @Nationalized
    @Column(name = "merchantid", length = 10)
    private String merchantid;

    @Nationalized
    @Column(name = "merchanttradenno", length = 20)
    private String merchanttradenno;

    @Nationalized
    @Column(name = "paymenttype", length = 20)
    private String paymenttype;

    @Nationalized
    @Column(name = "tradedesc", length = 200)
    private String tradedesc;

    @Nationalized
    @Column(name = "itemname", length = 400)
    private String itemname;

    @Nationalized
    @Column(name = "returnurl", length = 200)
    private String returnurl;

    @Nationalized
    @Column(name = "checkmacvalue", length = 200)
    private String checkmacvalue;

    @Column(name = "encrypttype")
    private Integer encrypttype;

    @Nationalized
    @Column(name = "clientbackurl", length = 200)
    private String clientbackurl;

    @Nationalized
    @Column(name = "itemurl", length = 200)
    private String itemurl;

    @Nationalized
    @Column(name = "orderresulturl", length = 200)
    private String orderresulturl;

    @Nationalized
    @Column(name = "needextrapaidinfo", length = 1)
    private String needextrapaidinfo;

    @OneToMany(mappedBy = "orderid")
    private List<OrderDetail > orderDetail;

}