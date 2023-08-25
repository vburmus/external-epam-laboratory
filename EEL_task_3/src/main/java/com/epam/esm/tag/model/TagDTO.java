package com.epam.esm.tag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO extends RepresentationModel<TagDTO> {
    private Long id;
    private String name;
    private String imageURL;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TagDTO tag = (TagDTO) o;
        return id != null && Objects.equals(id, tag.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}