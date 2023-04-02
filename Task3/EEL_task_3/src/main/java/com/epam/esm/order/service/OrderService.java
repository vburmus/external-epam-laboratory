package com.epam.esm.order.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    public OrderService(OrderRepository orderRepository, GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    public Page<Order> getAllOrders(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(--page, size);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(orderRepository.findAll(pageRequest));
    }


    public String getOrderInfoByID(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) throw new NoSuchItemException("No such order!");
        return order.get().toString();
    }

    @Transactional
    public Order createOrder(Order order) {

        if (orderRepository.exists(Example.of(order))) throw new ObjectIsInvalidException("Order already exists!");

        if (!ParamsValidation.isValidOrder(order)) {
            throw new ObjectIsInvalidException("Order is invalid!");
        }
        BigDecimal cost = BigDecimal.ZERO;
        for (GiftCertificate gc : order.getGiftCertificates()) {
            cost = cost.add(giftCertificateRepository.getPriceById(gc.getId()));
        }
        order.setCost(cost);
        orderRepository.save(order);
        List<GiftCertificate> unknownGCs = new ArrayList<>();
        for (GiftCertificate gc : order.getGiftCertificates()) {
            if (orderRepository.existsByIdAndGiftCertificateId(gc.getId(), order.getId()))
                orderRepository.incrementQuantityByGiftCertificateIdAndOrderId(gc.getId(), order.getId());
            else unknownGCs.add(gc);
        }
        order.getGiftCertificates().addAll(unknownGCs);
        orderRepository.save(order);
        return order;
    }
}
