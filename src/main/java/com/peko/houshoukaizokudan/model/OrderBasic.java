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
@Table(name = "OrderBasicData")
public class OrderBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerid")
    @ToString.Exclude
    private Member sellerid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid")
    @ToString.Exclude
    private Member memberid;

    @Nationalized
    @Column(name = "merchanttradedate", length = 20)
    private String merchanttradedate;

    @Nationalized
    @Column(name = "choosepayment", length = 20)
    private String choosepayment;

    @Column(name = "totalamount")
    private Integer totalamount;

    @Column(name = "rating")
    private Integer rating;

    @Nationalized
    @Column(name = "reviewcontent", length = 400)
    private String reviewcontent;

    @Nationalized
    @Column(name = "merchantid", length = 10)
    private String merchantid;

    @Nationalized
    @Column(name = "merchanttradenno", length = 20)
    private String merchanttradenno;

    @Nationalized
    @Column(name = "paymenttype", length = 20)
    private String paymenttype;

    @Nationalized
    @Column(name = "tradedesc", length = 200)
    private String tradedesc;

    @Nationalized
    @Column(name = "itemname", length = 400)
    private String itemname;

    @Nationalized
    @Column(name = "returnurl", length = 200)
    private String returnurl;

    @Column(name = "checkmacvalue")
    private Character checkmacvalue;

    @Column(name = "encrypttype")
    private Integer encrypttype;

    @Nationalized
    @Column(name = "clientbackurl", length = 200)
    private String clientbackurl;

    @Nationalized
    @Column(name = "itemurl", length = 200)
    private String itemurl;

    @Nationalized
    @Column(name = "orderresulturl", length = 200)
    private String orderresulturl;

    @Nationalized
    @Column(name = "needextrapaidinfo", length = 1)
    private String needextrapaidinfo;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderBasic that = (OrderBasic) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}