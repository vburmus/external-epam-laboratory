package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
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
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.Constants.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class GiftCertificateRepositoryTest {


    @Autowired
    private GiftCertificateRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        GiftCertificate gc1 =
                GiftCertificate.builder().name(GC_1).price(new BigDecimal(5)).longDescription(TEST_DESCRIPTION).durationDate(LocalDateTime.MAX).build();
        entityManager.persist(gc1);
        Tag tag = Tag.builder().name(TAG_1).build();
        tag = entityManager.persist(tag);
        GiftCertificate gc2 =
                GiftCertificate.builder().name(GC_2).price(new BigDecimal(5)).tags(List.of(tag)).longDescription(
                        TEST_DESCRIPTION).durationDate(LocalDateTime.MAX).build();
        entityManager.persist(gc2);
        GiftCertificate gc3 =
                GiftCertificate.builder().name(GC_3).price(new BigDecimal(5)).longDescription(OTHER_DESCRIPTION).durationDate(LocalDateTime.MAX).build();
        entityManager.persist(gc3);
    }

    @Test
    void testFindByNameDescriptionContaining() {

        Page<GiftCertificate> page = repository.findByNameContaining(PARTIAL_NAME, PageRequest.of(0, 10));
        List<GiftCertificate> certificates = page.getContent();

        Assertions.assertEquals(3, certificates.size());
        Assertions.assertEquals(GC_1, certificates.get(0).getName());

        page = repository.findByShortDescriptionContaining(PARTIAL_DESCRIPTION, PageRequest.of(0, 10));
        certificates = page.getContent();

        Assertions.assertEquals(2, certificates.size());
        Assertions.assertEquals(GC_1, certificates.get(0).getName());
    }

    @Test
    void testFindByTagIdAndName() {
        Page<GiftCertificate> page = repository.findByTagsIdIn(List.of(2L), PageRequest.of(0, 10));
        List<GiftCertificate> certificates = page.getContent();
        Assertions.assertEquals(1, certificates.size());
        page = repository.findByTagsName(TAG_1, PageRequest.of(0, 10));
        certificates = page.getContent();
        Assertions.assertEquals(1, certificates.size());
    }

}