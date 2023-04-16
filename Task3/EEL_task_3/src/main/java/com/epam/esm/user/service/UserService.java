package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<User> getAllUsers(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(--page, size);
        return userRepository.findAll(pageRequest);
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new NoSuchItemException("No such user!");
        return user.get();
    }
}
