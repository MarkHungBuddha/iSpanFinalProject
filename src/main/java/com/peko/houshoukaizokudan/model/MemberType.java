package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Nationalized;

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
@Table(name = "MemberType", schema = "dbo")
public class MemberType {
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
    @Fetch(FetchMode.JOIN)
    private Set<Member> member;

}