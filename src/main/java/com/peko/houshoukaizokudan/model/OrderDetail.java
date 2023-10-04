package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "OrderDetailData", schema = "dbo")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderdetailid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid")
    private com.peko.houshoukaizokudan.model.OrderBasic orderid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private com.peko.houshoukaizokudan.model.ProductBasic productid;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unitprice", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid")
    private com.peko.houshoukaizokudan.model.OrderStatus statusid;

}