package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProductReview", schema = "dbo")
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    private ProductBasic productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid")
    private Member memberid;

    @Column(name = "rating")
    private Integer rating;

    @Nationalized
    @Column(name = "reviewcontent", length = 400)
    private String reviewcontent;

    @Nationalized
    @Column(name = "reviewtime", length = 50)
    private String reviewtime;

    @OneToOne
    @JoinColumn(name = "orderdetailid")
    private OrderDetail orderdetail;

}