package com.epam.esm.user.repository;

import com.epam.esm.user.model.User;
import com.epam.esm.utils.AppQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(AppQuery.User.GET_ALL_USERS, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User getUserByID(long id) {
        return jdbcTemplate.queryForObject(AppQuery.User.GET_USER_BY_ID, new BeanPropertyRowMapper<>(User.class), id);
    }
}
