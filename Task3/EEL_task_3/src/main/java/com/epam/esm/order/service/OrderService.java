package com.epam.esm.order.service;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

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
        return orderRepository.getOrdersInfoByID(id);
      }
}
