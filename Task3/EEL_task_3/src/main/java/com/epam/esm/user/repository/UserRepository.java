package com.epam.esm.user.repository;

import com.epam.esm.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User getUserByID(long id);
}
