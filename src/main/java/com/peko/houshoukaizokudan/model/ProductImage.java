package com.peko.houshoukaizokudan.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProductImage", schema = "dbo")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    @Fetch(FetchMode.JOIN)
    private ProductBasic productid;

    @Column(name = "imagepath", nullable = false, length = 200)
    private String imagepath;

    @Column(name = "orderID", nullable = false)
    private Integer orderID;

}