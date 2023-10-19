package com.peko.houshoukaizokudan.model;

import java.util.Set;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Member", schema = "dbo")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membertypeid")
    private MemberType membertypeid;

    @Nationalized
    @Column(name = "memberimgpath", nullable = false, length = 200)
    private String memberimgpath;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Nationalized
    @Column(name = "firstname", length = 50)
    private String firstname;

    @Nationalized
    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "gender", length = 10)
    private String gender;

    @Nationalized
    @Column(name = "passwdbcrypt", length = 100)
    private String passwdbcrypt;

    @Nationalized
    @Column(name = "birthdate", length = 50)
    private String birthdate;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Nationalized
    @Column(name = "membercreationdate", nullable = false, length = 50)
    private String membercreationdate;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "postalcode", length = 20)
    private String postalcode;

    @OneToMany(mappedBy = "sellermemberid")
    private Set<ProductBasic> productBasic ;

    @OneToMany(mappedBy = "memberid")
    private Set<ProductReview> productReview ;

    @OneToMany(mappedBy = "memberid")
    private Set<ShoppingCart> shoppingCart ;

    @OneToMany(mappedBy = "memberid")
    private Set<UserCoupon> userCoupon ;

    @OneToMany(mappedBy = "memberid")
    private Set<Wishlist> wishlist ;

    @OneToMany(mappedBy = "seller")
    
    private Set<OrderBasic> soldOrders;

    @OneToMany(mappedBy = "buyer")
    private Set<OrderBasic> boughtOrders;

    @OneToMany(mappedBy = "sellerMember")
    private Set<QandA> askedQuestions;

    @OneToMany(mappedBy = "buyerMember")
    private Set<QandA> receivedQuestions;

}