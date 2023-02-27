package com.epam.esm.giftcertificate.model;

import com.epam.esm.tag.model.Tag;


import java.util.List;
import java.util.Objects;


public class GiftCertificate {
    private Long id;
    private String name;
    private List<Tag> tags;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;

    public GiftCertificate() {
    }

    public GiftCertificate(Long id, String name, List<Tag> tags, String description, Double price, Integer duration) {
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }


    public Long getId() {
        return id;
    }

    public GiftCertificate setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GiftCertificate setName(String name) {
        this.name = name;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public GiftCertificate setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public GiftCertificate setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public GiftCertificate setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public GiftCertificate setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public GiftCertificate setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public GiftCertificate setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id.equals(that.id) && Objects.equals(name, that.name) && Objects.equals(tags, that.tags) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(duration, that.duration) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }



    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate='" + createDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                '}';
    }
}
