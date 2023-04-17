package com.epam.esm.order.model;

import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "purchase")
@EntityListeners(AuditingEntityListener.class)
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
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;
    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private List<GiftCertificateHasOrder> giftCertificateHasOrders;


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
