package com.epam.esm.order.repository;

import com.epam.esm.order.model.Order;
import com.epam.esm.user.model.User;
import com.epam.esm.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import static com.epam.esm.Constants.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class OrderRepositoryTest {


    @Autowired
    private OrderRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        User user1 = User.builder()
                .name(USER_1)
                .surname(USER_1)
                .build();
        entityManager.persist(user1);
        Order order1 = Order.builder()
                .description(ORDER_1)
                .isClosed(0)
                .cost(new BigDecimal(5))
                .user(user1)
                .build();
        entityManager.persist(order1);
        User user2 = User.builder()
                .name(USER_2)
                .surname(USER_2)
                .build();
        entityManager.persist(user2);
        Order order2 = Order.builder()
                .description(ORDER_2)
                .isClosed(0)
                .cost(new BigDecimal(5))
                .user(user2)
                .build();
        entityManager.persist(order2);
    }

    @Test
    void findAllByUser() {
        User user1 = userRepository.findById(1L).get();
        Page<Order> page = repository.findAllByUser(user1, PageRequest.of(0, 10));
        List<Order> orders = page.getContent();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(ORDER_1, orders.get(0).getDescription());
    }
}