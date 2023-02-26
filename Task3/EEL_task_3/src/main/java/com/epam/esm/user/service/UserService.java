package com.epam.esm.user.service;

import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(Integer page) {
        return userRepository.getAllUsers(page);
    }

    public User getUserById(long id) {
        return userRepository.getUserByID(id);
    }
}
