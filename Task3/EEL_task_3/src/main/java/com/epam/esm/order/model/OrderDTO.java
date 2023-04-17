package com.epam.esm.order.model;

import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
}
