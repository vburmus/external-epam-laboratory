package com.epam.esm.user.controller;

import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.order.service.OrderService;
import com.epam.esm.user.model.UserDTO;
import com.epam.esm.user.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.epam.esm.utils.Constants.DEFAULT_PAGE;
import static com.epam.esm.utils.Constants.DEFAULT_SIZE;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Profile("default")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                 @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(userService.getAllUsers(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderDTO> createNewOrder(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
    }
}