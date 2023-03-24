package com.epam.esm.user.controller;

import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
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
    public CollectionModel<User> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        List<User> users = userService.getAllUsers(page, size);
        for (final User user : users) {
            Link selfLink = linkTo(methodOn(UserHateoasController.class).getUserById(user.getId())).withSelfRel();
            user.add(selfLink);
        }

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserHateoasController.class).showAll(page, size)).withSelfRel());
        if (page != 1)
            links.add(linkTo(methodOn(UserHateoasController.class).showAll(page - 1, size)).withSelfRel());
        links.add(linkTo(methodOn(UserHateoasController.class).showAll(page + 1, size)).withSelfRel());

        return CollectionModel.of(users, links);
    }

    @GetMapping("/search/{id}")
    public RepresentationModel<?> getUserById(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        Link selfLink = linkTo(methodOn(UserHateoasController.class).getUserById(id)).withSelfRel();
        return RepresentationModel.of(user, Collections.singleton(selfLink));
    }


}
