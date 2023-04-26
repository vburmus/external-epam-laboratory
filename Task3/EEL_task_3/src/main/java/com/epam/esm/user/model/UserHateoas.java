package com.epam.esm.user.model;

import com.epam.esm.order.model.Order;
import com.epam.esm.user.controller.UserHateoasController;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@NoArgsConstructor
@AllArgsConstructor

public class UserHateoas extends RepresentationModel<UserHateoas> {
    private Long id;
    private String name;
    private String surname;
    private String number;
    private List<Order> orders;

    public UserHateoas(UserDTO userDTO) {
        this.id = userDTO.getId();
        this.name = userDTO.getName();
        this.surname = userDTO.getSurname();
        this.number = userDTO.getNumber();
        this.orders = userDTO.getOrders();
        Link selfLink = linkTo(methodOn(UserHateoasController.class).getUserById(userDTO.getId())).withSelfRel();
        this.add(selfLink);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
