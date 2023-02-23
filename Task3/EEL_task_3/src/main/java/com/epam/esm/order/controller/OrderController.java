package com.epam.esm.order.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
