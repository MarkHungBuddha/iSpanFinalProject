package com.peko.houshoukaizokudan.model;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productid")
    private ProductBasic productid;

    @Column(name = "imagepath", nullable = false, length = 200)
    private String imagepath;

    public ProductImage(ProductBasic productid, String extractedCode) {
        this.productid = productid;
        this.imagepath = "https://i.imgur.com/" + extractedCode + ".png";
    }
}