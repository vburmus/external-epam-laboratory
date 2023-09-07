package com.epam.esm.user.controller;

import com.epam.esm.user.model.UserHateoas;
import com.epam.esm.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.utils.Constants.DEFAULT_PAGE;
import static com.epam.esm.utils.Constants.DEFAULT_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
@Profile("hateoas")
public class UserHateoasController {

    private final UserService userService;

    public UserHateoasController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<UserHateoas> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        Page<UserHateoas> usersPage = userService.getAllUsers(--page, size).map(UserHateoas::new);

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserHateoasController.class).showAll(page, size)).withSelfRel());
        if (page != 1)
            links.add(linkTo(methodOn(UserHateoasController.class).showAll(page - 1, size)).withSelfRel());
        if (usersPage.hasNext())
            links.add(linkTo(methodOn(UserHateoasController.class).showAll(page + 1, size)).withSelfRel());
        return CollectionModel.of(usersPage, links);
    }

    @GetMapping("/search/{id}")
    public RepresentationModel<UserHateoas> getUserById(@PathVariable("id") long id) {
        return new UserHateoas(userService.getUserById(id));
    }
}