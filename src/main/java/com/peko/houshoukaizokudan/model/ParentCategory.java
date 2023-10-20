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
@Table(name = "ParentCategory", schema = "dbo")
public class ParentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parentid", nullable = false)
    private Integer id;

    @Column(name = "parentname", nullable = false, length = 50)
    private String parentname;

    @OneToMany(mappedBy = "parentid")
    @Fetch(FetchMode.JOIN)
    private Set<ProductCategory> productCategory ;

}