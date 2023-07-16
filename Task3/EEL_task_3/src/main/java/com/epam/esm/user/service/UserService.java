package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exceptions.rest.NoSuchItemException;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserDTO;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.epam.esm.utils.Constants.USER_DOESNT_EXIST;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EntityToDtoMapper entityToDtoMapper;

    public UserService(UserRepository userRepository, EntityToDtoMapper entityToDtoMapper) {
        this.userRepository = userRepository;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    public Page<UserDTO> getAllUsers(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(--page, size);
        Page<User> allUsers = userRepository.findAll(pageRequest);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(allUsers).map(entityToDtoMapper::toUserDTO);
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NoSuchItemException(USER_DOESNT_EXIST);
        return entityToDtoMapper.toUserDTO(user.get());
    }
}