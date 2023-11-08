package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "OrderStatus", schema = "dbo")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusid", nullable = false)
    private Integer id;

    @Column(name = "statusname", nullable = false, length = 50)
    private String statusname;

    @OneToMany(mappedBy = "statusid")
    private Set<OrderBasic> orderBasics ;

}