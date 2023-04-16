package com.epam.esm.giftcertificatehasorder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GiftCertificateOrderID implements Serializable {
    @Column(name = "gift_certificate_id")
    private Long giftCertificateId;

    @Column(name = "order_id")
    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateOrderID that = (GiftCertificateOrderID) o;
        return Objects.equals(giftCertificateId, that.giftCertificateId) &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giftCertificateId, orderId);
    }
}
