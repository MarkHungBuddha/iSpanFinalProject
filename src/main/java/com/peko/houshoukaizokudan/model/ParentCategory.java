package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ParentCategory", schema = "dbo")
public class ParentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parentid", nullable = false)
    private Integer id;

    @Column(name = "parentname", nullable = false, length = 50)
    private String parentname;

    @OneToMany(mappedBy = "parentid")
    private Set<ProductCategory> productCategory ;

    @OneToMany(mappedBy = "parentid")
    private Set<ProductBasic> productBasic ;



}