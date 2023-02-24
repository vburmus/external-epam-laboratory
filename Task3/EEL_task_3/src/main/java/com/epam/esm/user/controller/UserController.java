package com.epam.esm.user.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<?> showAll(){
        return ResponseEntity.ok(Map.of("users",userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PostMapping("/create-order")
    public ResponseEntity<?> createNewOrder(@RequestBody Order order){
        return new ResponseEntity<>(Map.of("Order:", orderService.createOrder(order)), HttpStatus.CREATED);
    }
}
