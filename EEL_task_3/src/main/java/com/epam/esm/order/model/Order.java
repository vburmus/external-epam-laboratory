package com.epam.esm.order.model;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.user.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

public class Order extends RepresentationModel<Order> {
    private long id;

    private User user;

    private List<GiftCertificate> certificates;
    private String description;
    private boolean isClosed;
    private BigDecimal cost;
    private String createDate;
    private String lastUpdateDate;
    public Order() {
    }

    public Order(long id,User user, List<GiftCertificate> certificates, String description, boolean isClosed,  String createDate, String lastUpdateDate) {
        this.id = id;
        this.user = user;
        this.certificates = certificates;
        this.description = description;
        this.isClosed = isClosed;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public long getId() {
        return id;
    }

    public Order setId(long id) {
        this.id = id;
        return this;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return user;
    }

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    public Order setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Order setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public Order setClosed(boolean closed) {
        isClosed = closed;
        return this;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Order setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Order setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public Order setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder().append(id, order.id).append(isClosed, order.isClosed).append(certificates, order.certificates).append(description, order.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(certificates).append(description).append(isClosed).toHashCode();
    }

    @Override
    public String toString() {
        return "Cost:" + cost +
                ", datetime:" + createDate;

    }
}
