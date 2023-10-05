package com.peko.houshoukaizokudan.model;

import com.peko.houshoukaizokudan.model.Member;
import com.peko.houshoukaizokudan.model.ProductBasic;
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
@Table(name = "QandA")
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
    private Member sellerMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyermemberid")
    @ToString.Exclude
    private Member buyerMember;

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


}