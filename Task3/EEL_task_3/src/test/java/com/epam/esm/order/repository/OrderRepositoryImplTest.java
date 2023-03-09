package com.epam.esm.order.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepositoryImpl;
import com.epam.esm.order.model.Order;
import com.epam.esm.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryImplTest {

    public static final long ID3 = 3L;
    public static final long ID = 1L;
    public static final BigDecimal COST = new BigDecimal("12");
    public static final String DESCRIPTION = "...";
    public static final boolean CLOSED = false;
    public static final String CREATE_DATE = "2023-01-15 00:36:20.0";
    public static final String LAST_UPDATE_DATE = "2023-01-16 01:20:09";
    public static final long ID2 = 2L;
    public static final BigDecimal COST2 = new BigDecimal("1");
    private OrderRepositoryImpl orderRepository;
    private EmbeddedDatabase embeddedDatabase;
    private GiftCertificateRepository giftCertificateRepository;
    @BeforeEach
    public void init(){
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embeddedDB/task_3.sql")
                .addScript("embeddedDB/insert-some-data.sql")
                .build();
        this.orderRepository = new OrderRepositoryImpl(new JdbcTemplate(embeddedDatabase));
        this.giftCertificateRepository = new GiftCertificateRepositoryImpl(new JdbcTemplate(embeddedDatabase));
    }
    @Test
    void getAllOrders() {
        Order order1 = new Order();
        order1.setId(1).setUser(new User().setId(ID)).setCost(COST2).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate("2023-01-15 00:36:20").setLastUpdateDate(LAST_UPDATE_DATE);
        Order order2 = new Order();
        order2.setId(ID2).setUser(new User().setId(ID2)).setCost(COST).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate("2023-01-15 00:36:20").setLastUpdateDate(LAST_UPDATE_DATE);
        assertEquals(List.of(order1,order2),orderRepository.getAllOrders(1,10));
    }

    @Test
    void getOrdersByUsersID() {
        Order order1 = new Order();
        order1.setId(ID).setUser(new User().setId(ID)).setCost(COST2).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate("2023-01-15 00:36:20").setLastUpdateDate(LAST_UPDATE_DATE);
        Order order2 = new Order();
        order2.setId(ID2).setUser(new User().setId(ID2)).setCost(COST).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate("2023-01-15 00:36:20").setLastUpdateDate(LAST_UPDATE_DATE);
        assertEquals(List.of(order1),orderRepository.getOrdersByUserID(ID,1,10));
    }

    @Test
    void getOrderByID() {
        Order order1 = new Order();
        order1.setId(ID).setUser(new User().setId(ID)).setCost(COST).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate(CREATE_DATE).setLastUpdateDate(LAST_UPDATE_DATE);
        assertEquals(order1, orderRepository.getOrderByID(ID));
    }

    @Test
    void getOrdersID() {
        Order order1 = new Order();
        order1.setId(1).setUser(new User().setId(ID)).setCost(COST).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate(CREATE_DATE).setLastUpdateDate(LAST_UPDATE_DATE);
        assertEquals(order1, orderRepository.getOrderByID(ID));
    }



    @Test
    void createOrder() {
        Order order1 = new Order();
        order1.setId(ID3).setUser(new User().setId(ID)).setCost(COST).setClosed(CLOSED).setDescription(DESCRIPTION).setCreateDate(CREATE_DATE).setLastUpdateDate(LAST_UPDATE_DATE);
        assertTrue(orderRepository.createOrder(order1));
        GiftCertificate gc1 = giftCertificateRepository.getGiftCertificateByID(ID);
        orderRepository.setCertificateIntoOrder(gc1,order1);
        assertTrue(orderRepository.isOrderExist(order1));
        assertTrue( orderRepository.isCertificateExistsInOrder(gc1,order1));
        assertEquals(ID3,orderRepository.getOrderID(order1));
    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}