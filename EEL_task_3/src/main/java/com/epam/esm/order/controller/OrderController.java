package com.epam.esm.order.controller;

import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.order.service.OrderService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.utils.Constants.DEFAULT_PAGE;
import static com.epam.esm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequestMapping(value = "/order")
@Profile("default")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                  @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(orderService.getAllOrders(page, size), HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<String> getOrderInfoByID(@PathVariable("id") long id) {
        return new ResponseEntity<>(orderService.getOrderInfoByID(id), HttpStatus.OK);
    }

    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByUsersID(@PathVariable("id") long id,
                                                             @RequestParam(required = false, defaultValue =
                                                                     DEFAULT_PAGE) Integer page,
                                                             @RequestParam(required = false, defaultValue =
                                                                     DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(orderService.getOrdersByUsersID(id, page, size), HttpStatus.OK);
    }
}