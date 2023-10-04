package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "ShoppingCartData", schema = "dbo")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid")
    private Member memberid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private ProductBasic productid;

    @Column(name = "quantity")
    private Integer quantity;

}