package com.epam.esm.order.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final String ORDERS = "Orders:";
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping({"", "/{page}","/{page}/{size}"})
    public ResponseEntity<?> showAll(@PathVariable(required = false) Optional<Integer> page,@PathVariable(required = false) Optional<Integer> size){
        return isNotFound(orderService.getAllOrders(ParamsValidation.isValidPage(page),ParamsValidation.isValidSize(size)));
    }
    @GetMapping({"/by-user-id/{id}","/by-user-id/{id}/{page}","/byUser/{id}/{page}"})
    public ResponseEntity<?> getOrdersByUsersID(@PathVariable("id") long id,@PathVariable(required = false) Optional<Integer> page,@PathVariable(required = false) Optional<Integer> size){
        return isNotFound(orderService.getOrdersByUsersID(id,ParamsValidation.isValidPage(page),ParamsValidation.isValidSize(size)));
    }
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getOrdersInfoByID(@PathVariable("id") long id){
        return ResponseEntity.ok(Map.of("info:", orderService.getOrderInfoByID(id)));
    }
    public ResponseEntity<?> isNotFound(List<Order> orders) {
        if (orders.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(Map.of(ORDERS, orders));
    }
}
