package com.epam.esm.order.controller;

import com.epam.esm.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.epam.esm.utils.Constants.*;
import static com.epam.esm.utils.datavalidation.ParamsValidation.isNotFound;

@RestController
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return isNotFound(orderService.getAllOrders(page, size));
    }

    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<?> getOrdersByUsersID(@PathVariable("id") long id, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return isNotFound(orderService.getOrdersByUsersID(id, page, size));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(INFO, orderService.getOrderInfoByID(id)), HttpStatus.OK);
    }

}
