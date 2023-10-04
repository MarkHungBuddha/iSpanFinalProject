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
@ToString
@Entity
@Table(name = "OrderStatusData", schema = "dbo")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusid", nullable = false)
    private Integer id;

    @Column(name = "statusname", nullable = false, length = 50)
    private String statusname;

    @OneToMany(mappedBy = "statusid")
    private List<OrderDetail> orderDetail ;

}