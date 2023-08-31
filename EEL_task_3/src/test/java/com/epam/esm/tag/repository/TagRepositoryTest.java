package com.epam.esm.tag.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import com.epam.esm.giftcertificatehasorder.model.GiftCertificateOrderID;
import com.epam.esm.order.model.Order;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.Constants.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TagRepositoryTest {


    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        Tag tag1 = Tag.builder().name(TAG_1).build();
        Tag tag2 = Tag.builder().name(TAG_2).build();
        entityManager.persist(tag1);
        entityManager.persist(tag2);
        GiftCertificate gc1 = GiftCertificate.builder()
                .name(GC_1)
                .tags(List.of(tag1, tag2))
                .price(new BigDecimal(5))
                .longDescription(TEST_DESCRIPTION)
                .durationDate(LocalDateTime.MAX)
                .build();
        entityManager.persist(gc1);
        GiftCertificate gc2 = GiftCertificate.builder()
                .name(GC_1)
                .tags(List.of(tag2))
                .price(new BigDecimal(5))
                .longDescription(TEST_DESCRIPTION)
                .durationDate(LocalDateTime.MAX)
                .build();
        entityManager.persist(gc2);
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

        entityManager.persist(giftCertificateHasOrder);
        order1.setGiftCertificateHasOrders(List.of(giftCertificateHasOrder));

        Order order2 = Order.builder().description(ORDER_2).isClosed(0).cost(new BigDecimal(5)).user(user1).build();
        entityManager.persist(order2);
        GiftCertificateOrderID giftCertificateOrderID2 = new GiftCertificateOrderID();
        giftCertificateOrderID.setGiftCertificateId(gc2.getId());
        giftCertificateOrderID.setOrderId(order2.getId());
        GiftCertificateHasOrder giftCertificateHasOrder2 =
                GiftCertificateHasOrder.builder()
                        .id(giftCertificateOrderID2)
                        .giftCertificate(gc2)
                        .quantity(5)
                        .order(order2)
                        .build();

        entityManager.persist(giftCertificateHasOrder2);
        order2.setGiftCertificateHasOrders(List.of(giftCertificateHasOrder2));
    }

    @Test
    void existsByName() {
        Assertions.assertTrue(tagRepository.existsByName(TAG_1));
    }

    @Test
    void getMostUsedTag() {
        Optional<Tag> tag = tagRepository.findOne(Example.of(Tag.builder().name(TAG_2).build()));
        Assertions.assertEquals(tag, tagRepository.getMostUsedTag());
    }
}