package com.peko.houshoukaizokudan.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "QandAData")
public class QandA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productquestionid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid")
    @ToString.Exclude
    private ProductBasic productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellermemberid")
    @ToString.Exclude
    private Member sellermemberid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyermemberid")
    @ToString.Exclude
    private Member buyermemberid;

    @Nationalized
    @Column(name = "question", length = 400)
    private String question;

    @Nationalized
    @Column(name = "answer", length = 400)
    private String answer;

    @Nationalized
    @Column(name = "questiontime", length = 100)
    private String questiontime;

    @Nationalized
    @Column(name = "answertime", length = 100)
    private String answertime;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        QandA qandA = (QandA) o;
        return getId() != null && Objects.equals(getId(), qandA.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}