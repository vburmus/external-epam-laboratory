package com.epam.esm.user.controller;

import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping("/user")
@Profile("default")
public class UserController {


    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, userService.getAllUsers(page, size)), HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(USER, userService.getUserById(id)), HttpStatus.OK);
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(Map.of(ORDER, orderService.createOrder(orderDTO)), HttpStatus.CREATED);
    }


}
