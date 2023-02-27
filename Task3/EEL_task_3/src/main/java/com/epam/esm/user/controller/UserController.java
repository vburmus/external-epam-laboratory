package com.epam.esm.user.controller;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.model.User;
import com.epam.esm.user.service.UserService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    public static final String USERS = "Users";
    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }
    @GetMapping({"","/{page}","/{page}/{size}"})
    public ResponseEntity<?> showAll(@PathVariable(required = false) Optional<Integer> page,@PathVariable(required = false) Optional<Integer> size){
        List<User> users = userService.getAllUsers(ParamsValidation.isValidPage(page),ParamsValidation.isValidSize(size));
        if (users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(Map.of(USERS, users));
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PostMapping("/create-order")
    public ResponseEntity<?> createNewOrder(@RequestBody Order order){
        return new ResponseEntity<>(Map.of("Order:", orderService.createOrder(order)), HttpStatus.CREATED);
    }
}
