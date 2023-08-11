package com.epam.esm.giftcertificate.model;

import com.epam.esm.order.controller.OrderHateoasController;
import com.epam.esm.tag.model.Tag;
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
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateHateoas extends RepresentationModel<GiftCertificateHateoas> {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private List<Tag> tags;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    public GiftCertificateHateoas(GiftCertificateDTO giftCertificateDTO) {
        this.id = giftCertificateDTO.getId();
        this.name = giftCertificateDTO.getName();
        this.description = giftCertificateDTO.getDescription();
        this.price = giftCertificateDTO.getPrice();
        this.duration = giftCertificateDTO.getDuration();
        this.tags = giftCertificateDTO.getTags();
        this.createDate = giftCertificateDTO.getCreateDate();
        this.lastUpdateDate = giftCertificateDTO.getLastUpdateDate();
        Link selfLink =
                linkTo(methodOn(OrderHateoasController.class).getOrdersInfoByID(giftCertificateDTO.getId())).withSelfRel();
        this.add(selfLink);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
