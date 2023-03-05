package com.epam.esm.user.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.esm.utils.Constants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }
    @GetMapping
    public CollectionModel<User> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size){
        List<User> users =  userService.getAllUsers(page, size);
        for(final User user: users){
            Link selfLink = linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel();
            user.add(selfLink);
        }

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).showAll(page, size)).withSelfRel());
        if (page != 1)
            links.add(linkTo(methodOn(UserController.class).showAll(page - 1, size)).withSelfRel());
        links.add(linkTo(methodOn(UserController.class).showAll(page + 1, size)).withSelfRel());

        return CollectionModel.of(users, links);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id){
        return new ResponseEntity<>(Map.of(USER,userService.getUserById(id)),HttpStatus.OK);
    }
    @PostMapping("/create-order")
    public ResponseEntity<?> createNewOrder(@RequestBody Order order){
        return new ResponseEntity<>(Map.of(ORDER, orderService.createOrder(order)), HttpStatus.CREATED);
    }
}
