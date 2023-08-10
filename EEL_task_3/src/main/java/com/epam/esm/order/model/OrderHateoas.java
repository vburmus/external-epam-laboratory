package com.epam.esm.order.model;

import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.order.controller.OrderHateoasController;
import com.epam.esm.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHateoas extends RepresentationModel<OrderHateoas> {
    private Long id;
    private String description;
    private int isClosed;
    private BigDecimal cost;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private User user;
    private List<GiftCertificateHasOrder> giftCertificateHasOrders;

    public OrderHateoas(OrderDTO orderDTO) {
        this.id = orderDTO.getId();
        this.description = orderDTO.getDescription();
        this.isClosed = orderDTO.getIsClosed();
        this.cost = orderDTO.getCost();
        this.createDate = orderDTO.getCreateDate();
        this.lastUpdateDate = orderDTO.getLastUpdateDate();
        this.user = orderDTO.getUser();
        this.giftCertificateHasOrders = orderDTO.getGiftCertificateHasOrders();
        Link selfLink =
                linkTo(methodOn(OrderHateoasController.class).getOrdersInfoByID(orderDTO.getId())).withSelfRel();
        this.add(selfLink);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}