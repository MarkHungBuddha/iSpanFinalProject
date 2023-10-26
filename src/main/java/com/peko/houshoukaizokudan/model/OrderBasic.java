package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    @Fetch(FetchMode.JOIN)
    private Member seller; // 對應到 sellerid

    @ManyToOne
    @JoinColumn(name = "memberid")
    @Fetch(FetchMode.JOIN)
    private Member buyer; // 對應到 memberid

    @Nationalized
    @Column(name = "merchanttradedate", length = 20)
    private String merchanttradedate;


    @Column(name = "totalamount")
    private Integer totalamount;


    @Nationalized
    @Column(name = "orderaddress", length = 200) // 訂單地址
    private String orderaddress;


    @OneToMany(mappedBy = "orderid")
    private Set<OrderDetail> orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    @Fetch(FetchMode.JOIN)
    private OrderStatus statusid;


}