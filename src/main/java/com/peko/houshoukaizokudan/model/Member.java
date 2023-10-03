package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
@Data
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "MemberData")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberid", nullable = false)
    private Integer id;

    @Column(name = "membertypeid")
    private Integer membertypeid;

    @Nationalized
    @Column(name = "memberimgpath", nullable = false, length = 200)
    private String memberimgpath;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Nationalized
    @Column(name = "firstname", length = 50)
    private String firstname;

    @Nationalized
    @Column(name = "lastname", length = 50)
    private String lastname;

    @Column(name = "gender", length = 10)
    private String gender;

    @Nationalized
    @Column(name = "passwdbcrypt", length = 100)
    private String passwdbcrypt;

    @Nationalized
    @Column(name = "birthdate", length = 50)
    private String birthdate;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Nationalized
    @Column(name = "membercreationdate", nullable = false, length = 50)
    private String membercreationdate;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "street", length = 100)
    private String street;

    @Column(name = "postalcode", length = 20)
    private String postalcode;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Member member = (Member) o;
        return getId() != null && Objects.equals(getId(), member.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}