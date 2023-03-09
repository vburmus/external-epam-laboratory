package com.epam.esm.order.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    public OrderService(OrderRepository orderRepository, GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public List<Order> getAllOrders(Integer page,Integer size) {
        return ParamsValidation.isEmptyOrElseThrowPageException(orderRepository.getAllOrders(page,size));
    }


    public List<Order> getOrdersByUsersID(long usersId,Integer page,Integer size) {
        return ParamsValidation.isEmptyOrElseThrowPageException(orderRepository.getOrdersByUsersID(usersId,page,size));
    }


    public String getOrderInfoByID(long id) {
       Order order = orderRepository.getOrderByID(id);
       if(order==null)
           throw new NoSuchItemException("No such order!");
       return order.toString();
      }
    @Transactional
    public Order createOrder(Order order) {

        if(orderRepository.isOrderExist(order)){
            throw new ObjectIsInvalidException("Order already exists!");
        }
        if(!ParamsValidation.isValidOrder(order)){
            throw new ObjectIsInvalidException("Order is invalid!");
        }
        BigDecimal cost = BigDecimal.ZERO;
        for(GiftCertificate gc: order.getCertificates()){
            cost = cost.add(giftCertificateRepository.getCertificatesPriceByID(gc.getId()));
        }
        order.setCost(cost);
        orderRepository.createOrder(order);
        order.setId(orderRepository.getOrdersID(order));

        for (GiftCertificate gc: order.getCertificates()) {
         if (orderRepository.isCertificateExistsInOrder(gc,order))
            orderRepository.incrementQuantity(gc,order);
         else
            orderRepository.setCertificateIntoOrder(gc, order);

        }
        return order;
    }
}
