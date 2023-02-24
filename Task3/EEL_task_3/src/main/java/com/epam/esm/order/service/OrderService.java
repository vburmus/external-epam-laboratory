package com.epam.esm.order.service;

import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }


    public List<Order> getOrdersByUsersID(long usersId) {
        return orderRepository.getOrdersByUsersID(usersId);
    }


    public String getOrderInfoByID(long id) {
        return orderRepository.getOrderByID(id).toString();
      }
    @Transactional
    public Order createOrder(Order order) {

        if(orderRepository.isOrderExist(order)){
            throw new ObjectIsInvalidException("Order already exists!");
        }
        if(!ParamsValidation.isValidOrder(order)){
            throw new ObjectIsInvalidException("Order is invalid!");
        }
        orderRepository.createOrder(order);
        order.setId(orderRepository.getOrdersID(order));
        for (GiftCertificate gc: order.getCertificates())
            orderRepository.setCertificateIntoOrder(gc,order);
        return order;
    }
}
