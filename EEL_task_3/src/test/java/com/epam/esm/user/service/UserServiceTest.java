package com.epam.esm.user.service;

import com.epam.esm.user.model.User;
import com.epam.esm.user.model.UserDTO;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import com.epam.esm.utils.mappers.EntityToDtoMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final EntityToDtoMapper entityMapper = new EntityToDtoMapperImpl();
    @Mock
    private EntityToDtoMapper entityToDtoMapper;
    @Mock
    private UserRepository userRepositoryMocked;
    @InjectMocks
    private UserService userServiceMocked;

    @Test
    void getAllUsers() {
        User user = User.builder().id(ID1).name(USER_1).build();
        List<User> users = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10), users.size());
        UserDTO userDTO = entityMapper.toUserDTO(user);
        List<UserDTO> usersDTO = Collections.singletonList(userDTO);
        Page<UserDTO> userDTOPage = new PageImpl<>(usersDTO, PageRequest.of(0, 10), usersDTO.size());
        when(entityToDtoMapper.toUserDTO(user)).thenReturn(userDTO);

        when(userRepositoryMocked.findAll(PageRequest.of(0, 10))).thenReturn(userPage);
        assertEquals(userDTOPage, userServiceMocked.getAllUsers(1, 10));
    }

    @Test
    void getUserById() {
        User user = User.builder().id(ID1).name(USER_1).build();
        when(userRepositoryMocked.findById(ID1)).thenReturn(Optional.ofNullable(user));
        when(entityToDtoMapper.toUserDTO(user)).thenReturn(entityMapper.toUserDTO(user));

        assertEquals(entityMapper.toUserDTO(user), userServiceMocked.getUserById(ID1));
    }
}
