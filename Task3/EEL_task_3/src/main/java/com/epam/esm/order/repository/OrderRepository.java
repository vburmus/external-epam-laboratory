package com.epam.esm.order.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders(Integer page);
    List<Order> getOrdersByUsersID(long id,Integer page);
    Order getOrderByID(long id);
    long getOrdersID(Order order);
    boolean isOrderExist(Order order);

    boolean createOrder(Order order);
    boolean setCertificateIntoOrder(GiftCertificate gc, Order order);
}
