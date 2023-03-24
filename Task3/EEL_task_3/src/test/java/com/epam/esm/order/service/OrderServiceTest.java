package com.epam.esm.order.service;

import com.epam.esm.order.model.Order;
import com.epam.esm.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    public static final long ID = 1L;
    @Mock
    private OrderRepository orderRepositoryMocked;
    @InjectMocks
    private OrderService orderServiceMocked;
    @Test
    void getAllOrdersAndByIDAndInfo() {
        Order order = new Order().setId(ID);
        when(orderRepositoryMocked.getAllOrders(1,10)).thenReturn(List.of(order));
        assertEquals(List.of(order), orderServiceMocked.getAllOrders(1,10));
        when(orderRepositoryMocked.getOrdersByUserID(ID,1,10)).thenReturn(List.of(order));
        assertEquals(List.of(order),orderServiceMocked.getOrdersByUsersID(ID,1,10));
        when(orderRepositoryMocked.getOrderByID(ID)).thenReturn(order);
        assertEquals(order.toString(), orderServiceMocked.getOrderInfoByID(ID));
    }

}