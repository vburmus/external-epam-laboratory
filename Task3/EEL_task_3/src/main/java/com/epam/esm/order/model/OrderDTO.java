package com.epam.esm.order.model;

import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String description;
    private int isClosed;
    private BigDecimal cost;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User user;
    private List<GiftCertificateHasOrder> giftCertificateHasOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderDTO order = (OrderDTO) o;
        return id != null && Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}