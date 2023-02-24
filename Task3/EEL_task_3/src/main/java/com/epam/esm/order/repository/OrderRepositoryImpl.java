package com.epam.esm.order.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;
import com.epam.esm.utils.AppQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
    public Order getOrderByID(long id) {
        return jdbcTemplate.queryForObject(AppQuery.Order.GET_ORDER_COST_AND_TIMESTAMP_BY_ID, new BeanPropertyRowMapper<>(Order.class),id);
    }

    @Override
    public long getOrdersID(Order order) {
        return jdbcTemplate.queryForObject(AppQuery.Order.GET_ORDERS_ID, Long.class, order.getUser().getId(), order.getDescription(),order.getCost());
    }

    @Override
    public boolean isOrderExist(Order order) {
        return jdbcTemplate.queryForObject(AppQuery.Order.IS_ORDER_EXIST, Integer.class,order.getUser().getId(),order.getDescription(),order.getCost()) >=1;
    }

    @Override
    public boolean createOrder(Order order) {
        LocalDateTime time = LocalDateTime.now();
        return jdbcTemplate.update(AppQuery.Order.CREATE_ORDER,order.getUser().getId(),order.getCost(),order.getDescription(),time,time)==1;
    }

    @Override
    public boolean setCertificateIntoOrder(GiftCertificate gc, Order order) {
        return jdbcTemplate.update(AppQuery.Order.ADD_GC_INTO_ORDER,gc.getId(),order.getId()) == 1;
    }
}
