package com.epam.esm.order.controller;

import com.epam.esm.order.service.OrderService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping("/order")
@Profile("default")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, orderService.getAllOrders(page, size)), HttpStatus.OK);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(INFO, orderService.getOrderInfoByID(id)), HttpStatus.OK);
    }

    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<?> getOrdersByUsersID(@PathVariable("id") long id, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, orderService.getOrdersByUsersID(id, page, size)), HttpStatus.OK);
    }
}
