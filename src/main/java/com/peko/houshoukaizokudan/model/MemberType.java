package com.peko.houshoukaizokudan.model;

import java.util.Set;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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