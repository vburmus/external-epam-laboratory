package com.epam.esm.order.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateOrderID;
import com.epam.esm.giftcertificatehasorder.repository.GiftCertificateHasOrderRepository;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
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
    private final UserRepository userRepository;
    private final GiftCertificateHasOrderRepository giftCertificateHasOrderRepository;
    private final EntityToDtoMapper entityToDtoMapper;

    public OrderService(OrderRepository orderRepository, GiftCertificateRepository giftCertificateRepository,
                        UserRepository userRepository, GiftCertificateHasOrderRepository giftCertificateHasOrderRepository,
                        EntityToDtoMapper entityToDtoMapper) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
        this.giftCertificateHasOrderRepository = giftCertificateHasOrderRepository;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = entityToDtoMapper.toOrder(orderDTO);
        Long userId = order.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchItemException("User not found with id " + userId));

        order.setUser(user);

        Order savedOrder = orderRepository.save(order);

        List<GiftCertificateHasOrder> giftCertificateHasOrders = new ArrayList<>();
        for (GiftCertificateHasOrder giftCertificateHasOrder : order.getGiftCertificateHasOrders()) {
            Long giftCertificateId = giftCertificateHasOrder.getGiftCertificate().getId();
            GiftCertificate giftCertificate =
                    giftCertificateRepository.findById(giftCertificateId).orElseThrow(() -> new NoSuchItemException("Gift certificate " +
                            "not" + " found with id " + giftCertificateId));

            GiftCertificateOrderID id = new GiftCertificateOrderID();
            id.setGiftCertificateId(giftCertificate.getId());
            id.setOrderId(savedOrder.getId());

            GiftCertificateHasOrder newGiftCertificateHasOrder = new GiftCertificateHasOrder();
            newGiftCertificateHasOrder.setId(id);
            newGiftCertificateHasOrder.setGiftCertificate(giftCertificate);
            newGiftCertificateHasOrder.setOrder(savedOrder);
            newGiftCertificateHasOrder.setQuantity(giftCertificateHasOrder.getQuantity());

            GiftCertificateHasOrder gcho = giftCertificateHasOrderRepository.save(newGiftCertificateHasOrder);
            giftCertificateHasOrders.add(gcho);
        }

        savedOrder.setGiftCertificateHasOrders(giftCertificateHasOrders);

        BigDecimal orderCost =
                giftCertificateHasOrders.stream().map(gcho -> gcho.getGiftCertificate().getPrice().multiply(new BigDecimal(gcho.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        savedOrder.setCost(orderCost);

        return entityToDtoMapper.toOrderDTO(savedOrder);
    }

    public Page<OrderDTO> getAllOrders(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(--page, size);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(orderRepository.findAll(pageRequest)).map(entityToDtoMapper::toOrderDTO);
    }


    public String getOrderInfoByID(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) throw new NoSuchItemException("No such order!");
        return order.get().toString();
    }


    public Page<OrderDTO> getOrdersByUsersID(long id, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(--page, size);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NoSuchItemException(String.format("No user with id = %d found", id));
        Page<Order> ordersByUSer = orderRepository.findAllByUser(user.get(), pageRequest);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(ordersByUSer).map(entityToDtoMapper::toOrderDTO);
    }
}

