package com.epam.esm.order.repository;

import com.epam.esm.order.model.Order;
import com.epam.esm.utils.AppQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class OrderRepositoryImpl implements OrderRepository{
    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> getAllOrders() {
        return jdbcTemplate.query(AppQuery.Order.GET_ALL_ORDERS,new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public List<Order> getOrdersByUsersID(long usersId) {
        return jdbcTemplate.query(AppQuery.Order.GET_ORDERS_BY_USER_ID,new BeanPropertyRowMapper<>(Order.class),usersId);
    }

    @Override
    public String getOrdersInfoByID(long id) {
        return jdbcTemplate.queryForObject(AppQuery.Order.GET_ORDER_COST_AND_TIMESTAMP_BY_ID, String.class,id);
    }
}
