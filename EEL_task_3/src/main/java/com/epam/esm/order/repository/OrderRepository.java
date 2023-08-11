package com.epam.esm.order.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders(Integer page,Integer size);
    List<Order> getOrdersByUserID(long id, Integer page, Integer size);
    Order getOrderByID(long id);
    long getOrderID(Order order);
    boolean isOrderExist(Order order);

    boolean createOrder(Order order);
    boolean setCertificateIntoOrder(GiftCertificate gc, Order order);
    boolean isCertificateExistsInOrder(GiftCertificate gc, Order order);

    boolean incrementQuantity(GiftCertificate gc, Order order);
}
