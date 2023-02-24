package com.epam.esm.order.repository;

import com.epam.esm.order.model.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders();
    List<Order> getOrdersByUsersID(long id);
    Order getOrderByID(long id);
    boolean isOrderExist(Order order);

    boolean createOrder(Order order);
}
