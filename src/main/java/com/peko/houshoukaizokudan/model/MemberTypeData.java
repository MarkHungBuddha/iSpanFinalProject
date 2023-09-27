package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "MemberTypeData", schema = "dbo")
public class MemberTypeData {
    @Id
    @Column(name = "membertypeid", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "membertypename", nullable = false, length = 50)
    private String membertypename;

    @Nationalized
    @Column(name = "memberTypeDescription", length = 50)
    private String memberTypeDescription;

    @OneToMany(mappedBy = "membertypeid")
    private Set<com.peko.houshoukaizokudan.model.MemberDatum> memberData = new LinkedHashSet<>();

}