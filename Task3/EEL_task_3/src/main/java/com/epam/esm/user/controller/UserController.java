package com.epam.esm.user.controller;

import com.epam.esm.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<?> showAll(){
        return ResponseEntity.ok(Map.of("users",userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
