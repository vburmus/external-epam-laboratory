package com.epam.esm.order.service;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateOrderID;
import com.epam.esm.giftcertificatehasorder.repository.GiftCertificateHasOrderRepository;
import com.epam.esm.order.model.Order;
import com.epam.esm.order.model.OrderDTO;
import com.epam.esm.order.repository.OrderRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import com.epam.esm.utils.mappers.EntityToDtoMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static com.epam.esm.Constants.*;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final EntityToDtoMapper entityMapper = new EntityToDtoMapperImpl();
    @Mock
    private EntityToDtoMapper entityToDtoMapper;
    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMocked;
    @Mock
    private OrderRepository orderRepositoryMocked;
    @Mock
    private UserRepository userRepositoryMocked;
    @Mock
    private GiftCertificateHasOrderRepository giftCertificateHasOrderRepositoryMocked;
    @InjectMocks
    private OrderService orderServiceMocked;

    @Test
    void getAllOrdersAndByIDAndInfo() {
        User user = User.builder()
                .id(ID1)
                .name(USER_1)
                .build();
        Order order = Order.builder()
                .id(ID1)
                .description(ORDER_1)
                .isClosed(0)
                .cost(new BigDecimal(5))
                .user(user)
                .build();
        OrderDTO orderDTO = entityMapper.toOrderDTO(order);
        List<Order> orderList = Collections.singletonList(order);
        Page<Order> page = new PageImpl<>(orderList, PageRequest.of(0, 10),
                orderList.size());
        List<OrderDTO> orderDtoList = Collections.singletonList(entityMapper.toOrderDTO(order));
        Page<OrderDTO> pageDTO = new PageImpl<>(orderDtoList, PageRequest.of(0, 10), orderDtoList.size());
        when(orderRepositoryMocked.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(entityToDtoMapper.toOrderDTO(order)).thenReturn(orderDTO);
        Page<OrderDTO> pageTest = orderServiceMocked.getAllOrders(1, 10);
        assertEquals(pageDTO, pageTest);

        when(orderRepositoryMocked.findAllByUser(user, PageRequest.of(0, 10))).thenReturn(page);
        when(userRepositoryMocked.findById(ID1)).thenReturn(Optional.of(user));
        when(entityToDtoMapper.toOrderDTO(order)).thenReturn(orderDTO);
        assertEquals(pageDTO, orderServiceMocked.getOrdersByUsersID(ID1, 1, 10));

        when(orderRepositoryMocked.findById(ID1)).thenReturn(Optional.of(order));
        assertEquals(order.toString(), orderServiceMocked.getOrderInfoByID(ID1));
    }

    @Test
    void createTest() {
        Tag tag1 = Tag.builder().name(TAG_1).build();
        Tag tag2 = Tag.builder().name(TAG_2).build();

        GiftCertificate gc1 = GiftCertificate.builder()
                .id(ID1)
                .name(GC_1)
                .tags(List.of(tag1, tag2))
                .price(new BigDecimal(5))
                .description(TEST_DESCRIPTION)
                .duration(5)
                .build();
        User user1 = User.builder()
                .id(ID1)
                .name(USER_1)
                .surname(USER_1)
                .build();
        Order order1 = Order.builder()
                .id(ID1)
                .description(ORDER_1)
                .isClosed(0)
                .cost(new BigDecimal(5))
                .user(user1)
                .build();
        GiftCertificateOrderID giftCertificateOrderID = new GiftCertificateOrderID();
        giftCertificateOrderID.setGiftCertificateId(gc1.getId());
        giftCertificateOrderID.setOrderId(order1.getId());
        GiftCertificateHasOrder giftCertificateHasOrder =
                GiftCertificateHasOrder.builder()
                        .id(giftCertificateOrderID)
                        .giftCertificate(gc1)
                        .quantity(5)
                        .order(order1)
                        .build();

        order1.setGiftCertificateHasOrders(List.of(giftCertificateHasOrder));

        OrderDTO orderDTO = entityMapper.toOrderDTO(order1);

        when(entityToDtoMapper.toOrder(orderDTO)).thenReturn(order1);
        when(userRepositoryMocked.findById(ID1)).thenReturn(Optional.of(user1));
        when(orderRepositoryMocked.save(order1)).thenReturn(order1);
        when(giftCertificateRepositoryMocked.findById(ID1)).thenReturn(Optional.of(gc1));
        when(giftCertificateHasOrderRepositoryMocked.save(giftCertificateHasOrder)).thenReturn(giftCertificateHasOrder);
        when(entityToDtoMapper.toOrderDTO(order1)).thenReturn(orderDTO);
        assertEquals(orderDTO, orderServiceMocked.createOrder(orderDTO));
    }

}
