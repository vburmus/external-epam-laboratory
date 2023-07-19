package com.epam.esm.order.controller;

import com.epam.esm.order.model.OrderHateoas;
import com.epam.esm.order.service.OrderService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
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
@RequestMapping("/order")
@Profile("hateoas")
public class OrderHateoasController {

    private final OrderService orderService;

    public OrderHateoasController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public CollectionModel<OrderHateoas> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                 @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        Page<OrderHateoas> ordersPage = orderService.getAllOrders(--page, size).map(OrderHateoas::new);

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(OrderHateoasController.class).showAll(page, size)).withSelfRel());
        if (page != 1)
            links.add(linkTo(methodOn(OrderHateoasController.class).showAll(page - 1, size)).withSelfRel());
        if (ordersPage.hasNext())
            links.add(linkTo(methodOn(OrderHateoasController.class).showAll(page + 1, size)).withSelfRel());

        return CollectionModel.of(ordersPage, links);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(INFO, orderService.getOrderInfoByID(id)), HttpStatus.OK);
    }
}