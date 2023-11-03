package com.peko.houshoukaizokudan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
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
    private Set<Member> member;
    

}