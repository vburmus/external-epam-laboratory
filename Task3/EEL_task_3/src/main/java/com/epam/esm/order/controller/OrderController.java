package com.epam.esm.order.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public ResponseEntity<?> showAll(){
        return ResponseEntity.ok(Map.of("orders:",orderService.getAllOrders()));
    }
    @GetMapping("/by-user-id/{id}")
    public ResponseEntity<?> getOrdersByUsersID(@PathVariable("id") long id){
        return ResponseEntity.ok(Map.of("users_orders:",orderService.getOrdersByUsersID(id)));
    }
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id){
        return ResponseEntity.ok(Map.of("info:", orderService.getOrderInfoByID(id)));
    }

}
