package com.epam.esm.user.service;

import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    public static final long ID = 1L;
    public static final String NAME = "Name";
    @Mock
    private UserRepositoryImpl userRepositoryMocked;
    @InjectMocks
    private UserService userServiceMocked;
    @Test
    void getAllUsers() {
        User user = new User().setId(ID).setName(NAME);
        when(userRepositoryMocked.getAllUsers(1,10)).thenReturn(List.of(user));
        assertEquals(List.of(user), userServiceMocked.getAllUsers(1,10));
    }

    @Test
    void getUserById() {
        User user = new User().setId(ID).setName(NAME);
        when(userRepositoryMocked.getUserByID(ID)).thenReturn(user);
        assertEquals(user,userServiceMocked.getUserById(ID));
    }
}