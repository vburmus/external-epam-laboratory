package com.epam.esm.giftcertificatehasorder.model;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "gift_certificate_has_order")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateHasOrder {
    @EmbeddedId
    private GiftCertificateOrderID id;

    @ManyToOne
    @MapsId("giftCertificateId")
    @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")
    private GiftCertificate giftCertificate;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiftCertificateHasOrder that = (GiftCertificateHasOrder) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
