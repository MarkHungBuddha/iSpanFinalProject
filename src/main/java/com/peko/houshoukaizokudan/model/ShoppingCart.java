package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "ShoppingCart", schema = "dbo")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid")
    @Fetch(FetchMode.JOIN)
    private Member memberid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    @Fetch(FetchMode.JOIN)
    private ProductBasic productid;

    @Column(name = "quantity")
    private Integer quantity;

}