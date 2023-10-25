package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProductCategory", schema = "dbo")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid", nullable = false)
    private Integer id;

    @Column(name = "categoryname", nullable = false, length = 50)
    private String categoryname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    @Fetch(FetchMode.JOIN)
    private ParentCategory parentid;

    @OneToMany(mappedBy = "categoryid")
    private Set<ProductBasic> productBasic ;

}