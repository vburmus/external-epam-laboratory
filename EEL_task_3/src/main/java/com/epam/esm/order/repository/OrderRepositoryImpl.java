package com.epam.esm.order.repository;

import com.epam.esm.exceptionhandler.exceptions.ObjectNotFoundException;
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
    public List<Order> getAllOrders(Integer page,Integer size) {
        return jdbcTemplate.query(AppQuery.getQueryWithPagination(AppQuery.Order.GET_ALL_ORDERS,page,size),new BeanPropertyRowMapper<>(Order.class));
    }

    @Override
    public List<Order> getOrdersByUserID(long usersId, Integer page, Integer size) {
        return jdbcTemplate.query(AppQuery.getQueryWithPagination(AppQuery.Order.GET_ORDERS_BY_USER_ID,page,size),new BeanPropertyRowMapper<>(Order.class),usersId);
    }

    @Override
    public Order getOrderByID(long id) {
        return jdbcTemplate.queryForObject(AppQuery.Order.GET_ORDER_COST_AND_TIMESTAMP_BY_ID, new BeanPropertyRowMapper<>(Order.class),id);
    }

    @Override
    public long getOrderID(Order order) {
        Long id = jdbcTemplate.queryForObject(AppQuery.Order.GET_ORDERS_ID, Long.class, order.getUser().getId(), order.getDescription(),order.getCost());
        if (id == null)
            throw new ObjectNotFoundException(String.format("Element with created on %s doesn't exist", order.getCreateDate()));
        else
            return id;
    }

    @Override
    public boolean isOrderExist(Order order) {
        Integer result = jdbcTemplate.queryForObject(AppQuery.Order.IS_ORDER_EXIST, Integer.class,order.getUser().getId(),order.getDescription(),order.getCost());
        return result != null && result == 1;
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

    @Override
    public boolean isCertificateExistsInOrder(GiftCertificate gc, Order order) {
        Integer result = jdbcTemplate.queryForObject(AppQuery.Order.IS_GC_IN_ORDER_EXIST,Integer.class, order.getId(), gc.getId());
        return result != null && result == 1;
    }

    @Override
    public boolean incrementQuantity(GiftCertificate gc, Order order) {
        return jdbcTemplate.update(AppQuery.Order.INCREMENT_QUANTITY,gc.getId(),order.getId()) == 1;
    }
}
