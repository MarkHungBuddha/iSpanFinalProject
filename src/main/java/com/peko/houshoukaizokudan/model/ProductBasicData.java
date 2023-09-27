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
@ToString
@Entity
@Table(name = "ProductBasicData", schema = "dbo")
public class ProductBasicData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellermemberid")
    private com.peko.houshoukaizokudan.model.MemberData sellermemberid;

    @Nationalized
    @Column(name = "productname", nullable = false, length = 1000)
    private String productname;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "specialprice", precision = 10, scale = 2)
    private BigDecimal specialprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private com.peko.houshoukaizokudan.model.ProductCategoryData categoryid;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Nationalized
    @Column(name = "description", length = 4000)
    private String description;

    @OneToMany(mappedBy = "productid")
    private List<OrderDetailData> orderDetailData;

    @OneToMany(mappedBy = "productid")
    private List<ProductImageData> productImageData;

    @OneToMany(mappedBy = "productid")
    private List<ProductReviewData> productReviewData ;

    @OneToMany(mappedBy = "productid")
    private List<QandAData> qandAData ;

    @OneToMany(mappedBy = "productid")
    private List<ShoppingCartData> shoppingCartData ;

    @OneToMany(mappedBy = "productid")
    private List<WishlistData> wishlistData ;

}