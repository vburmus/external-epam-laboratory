package com.epam.esm.order.model;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.user.model.User;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "purchase")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order extends RepresentationModel<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;
    @ToString.Exclude
    private String description;
    @ToString.Exclude
    @Column(name = "is_closed")
    private int isClosed;
    private BigDecimal cost;
    @CreatedDate
    private String createDate;
    @ToString.Exclude
    @LastModifiedDate
    private String lastUpdateDate;
    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "gift_certificate_has_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))

    private List<GiftCertificate> giftCertificates;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
