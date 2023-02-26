package com.epam.esm.order.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.model.User;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping({"", "/{page}"})
    public ResponseEntity<?> showAll(@PathVariable(required = false) Optional<Integer> page){
        return ResponseEntity.ok(Map.of("orders:",orderService.getAllOrders(ParamsValidation.isValidPage(page))));
    }
    @GetMapping({"/by-user-id/{id}","/by-user-id/{id}/{page}"})
    public ResponseEntity<?> getOrdersByUsersID(@PathVariable("id") long id,@PathVariable(required = false) Optional<Integer> page){
        return ResponseEntity.ok(Map.of("users_orders:",orderService.getOrdersByUsersID(id,ParamsValidation.isValidPage(page))));
    }
    @GetMapping({"/info/{id}","/info/{id}/{page}"})
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id){
        return ResponseEntity.ok(Map.of("info:", orderService.getOrderInfoByID(id)));
    }

}
