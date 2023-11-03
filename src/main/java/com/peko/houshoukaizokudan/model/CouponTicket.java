package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "CouponTicket", schema = "dbo")
public class CouponTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Integer id;

    @Column(name = "coupon_code", length = 20)
    private String couponCode;

    @Column(name = "coupon_name", length = 100)
    private String couponName;

    @Nationalized
    @Column(name = "coupon_description", length = 400)
    private String couponDescription;

    @Column(name = "minimum_amount")
    private Integer minimumAmount;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @OneToMany(mappedBy = "couponid")
    @ToString.Exclude
    private Set<UserCoupon> userCoupon;

}