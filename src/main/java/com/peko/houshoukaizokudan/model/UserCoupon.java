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
@Table(name = "UserCouponData", schema = "dbo")
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usercouponid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid")
    private Member memberid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couponid")
    private CouponTicket couponid;

    @Column(name = "used", nullable = false, length = 1)
    private String used;

}