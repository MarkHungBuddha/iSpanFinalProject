package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "ProductBasic", schema = "dbo")
public class ProductBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellermemberid")
    @Fetch(FetchMode.JOIN)
    private Member sellermemberid;

    @Nationalized
    @Column(name = "productname", nullable = false, length = 1000)
    private String productname;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "specialprice", precision = 10, scale = 2)
    private BigDecimal specialprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    @Fetch(FetchMode.JOIN)
    private ProductCategory categoryid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    @Fetch(FetchMode.JOIN)
    private ParentCategory  parentid;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @OneToMany(mappedBy = "productid")
    private Set<OrderDetail> orderDetail;

    @OneToMany(mappedBy = "productid")
    private Set<ProductImage> productImage;

    @OneToMany(mappedBy = "productid")
    private Set<ProductReview> productReview ;

    @OneToMany(mappedBy = "productid")
    private Set<QandA> qandA ;

    @OneToMany(mappedBy = "productid")
    private Set<ShoppingCart> shoppingCart ;

    @OneToMany(mappedBy = "productid")
    private Set<Wishlist> wishlist ;

}