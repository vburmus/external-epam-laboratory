package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(Integer page,Integer size) {
        return ParamsValidation.isEmptyOrElseThrowPageException(userRepository.getAllUsers(page,size));
    }

    public User getUserById(long id) {
        User user = userRepository.getUserByID(id);
        if(user==null)
            throw new NoSuchItemException("No such user!");
        return user;
    }
}
