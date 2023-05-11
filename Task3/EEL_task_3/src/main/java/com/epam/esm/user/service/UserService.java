package com.epam.esm.user.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectNotFoundException;
import com.epam.esm.exceptionhandler.exceptions.UserCreationFailureException;
import com.epam.esm.user.model.Role;
import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserDTO;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
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
        if (user.isEmpty()) throw new NoSuchItemException("No such user!");
        return entityToDtoMapper.toUserDTO(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }

    public void createUserFromAttributes(Map<String, Object> attributes, String provider) {
        String email = (String) attributes.get("email");

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            String[] fullName = attributes.get("name").toString().split(" ");
            User newUser = User.builder()
                    .name(fullName[0])
                    .surname(fullName[1])
                    .email(email)
                    .role(Role.USER)
                    .provider(provider)
                    .build();
            userRepository.save(newUser);
        }
        if(userRepository.findByEmail(email).isEmpty())
            throw new UserCreationFailureException("Failed to create or retrieve user");
    }
}

