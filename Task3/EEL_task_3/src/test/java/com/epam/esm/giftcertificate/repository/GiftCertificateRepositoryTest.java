package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateRepositoryTest {

    private GiftCertificateRepository giftCertificateRepository;
    private EmbeddedDatabase embeddedDatabase;

    @BeforeEach
    public void init() {
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embeddedDB/task_3.sql")
                .addScript("embeddedDB/insert-some-data.sql")
                .build();
        this.giftCertificateRepository = new GiftCertificateRepositoryImpl(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    void createGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setName("Test1");
        giftCertificate.setDescription("TestDescription");
        giftCertificate.setPrice(20);
        giftCertificate.setDuration(20);
        assertTrue(giftCertificateRepository.createGiftCertificate(giftCertificate));

    }

    @Test
    void getAllGiftCertificates() {
        GiftCertificate gc1 = new GiftCertificate();
        gc1.setId(1L);
        gc1.setName("GiftCertificate1");
        gc1.setDescription("description");
        gc1.setDuration(20);
        gc1.setPrice(20);
        gc1.setCreateDate("2023-01-15 00:36:20.0");
        gc1.setLastUpdateDate("2023-01-16 01:20:09.0");

        GiftCertificate gc2 = new GiftCertificate();
        gc2.setId(2L);
        gc2.setName("GiftCertificate2");
        gc2.setDescription("description");
        gc2.setDuration(20);
        gc2.setPrice(20);
        gc2.setCreateDate("2023-01-15 00:36:55.0");
        gc2.setLastUpdateDate("2023-01-16 01:25:34.0");

        assertEquals(List.of(gc1, gc2), giftCertificateRepository.getAllGiftCertificates(1,10));
    }

    @Test
    void getGiftCertificateByID() {
        GiftCertificate gc1 = new GiftCertificate();
        gc1.setId(1L);
        gc1.setName("GiftCertificate1");
        gc1.setDescription("description");
        gc1.setDuration(20);
        gc1.setPrice(20);
        gc1.setCreateDate("2023-01-15 00:36:20.0");
        gc1.setLastUpdateDate("2023-01-16 01:20:09.0");

        assertEquals(gc1, giftCertificateRepository.getGiftCertificateByID(1L));
    }

    @Test
    void isExistsGetIdAndDeleteGiftCertificate() {
        GiftCertificate gc1 = new GiftCertificate();
        gc1.setId(1L);
        gc1.setName("GiftCertificate1");
        gc1.setDescription("description");
        gc1.setDuration(20);
        gc1.setPrice(20);
        gc1.setCreateDate("2023-01-15 00:36:20.0");
        gc1.setLastUpdateDate("2023-01-16 01:20:09.0");
        assertTrue(giftCertificateRepository.isGiftCertificateExist(gc1));
        assertTrue(giftCertificateRepository.deleteGiftCertificate(giftCertificateRepository.getGiftCertificatesID(gc1)));
        assertFalse(giftCertificateRepository.isGiftCertificateExist(gc1));
    }

    @Test
    void updateGiftCertificate() {
        assertTrue(giftCertificateRepository.updateGiftCertificate(1L, Map.of("name", "newUpdate", "price", "222")));
        assertEquals("newUpdate", giftCertificateRepository.getGiftCertificateByID(1L).getName());
        assertEquals(222, giftCertificateRepository.getGiftCertificateByID(1L).getPrice());
    }

    @Test
    void deleteTagDependenciesCreateGetAllByCertificate() {
        Tag tag1 = new Tag();
        tag1.setName("Tag1");
        tag1.setId(1L);
        Tag tag2 = new Tag();
        tag2.setName("Tag2");
        tag2.setId(2L);
        assertEquals(List.of(tag1), giftCertificateRepository.getAllTagsIdByCertificateId(1L));
        assertEquals(List.of(tag2), giftCertificateRepository.getAllTagsIdByCertificateId(2L));
        assertTrue(giftCertificateRepository.deleteTagDependenciesForGiftCertificate(List.of(tag1.getId()), 1L));
        assertTrue(giftCertificateRepository.deleteTagDependenciesForGiftCertificate(List.of(tag2.getId()), 2L));
        assertEquals(List.of(), giftCertificateRepository.getAllTagsIdByCertificateId(1L));
        assertEquals(List.of(), giftCertificateRepository.getAllTagsIdByCertificateId(2L));
        assertTrue(giftCertificateRepository.createTagDependenciesForGiftCertificate(List.of(tag1.getId(), tag2.getId()), 1L));
        assertTrue(giftCertificateRepository.createTagDependenciesForGiftCertificate(List.of(tag1.getId(), tag2.getId()), 2L));
        assertEquals(List.of(tag1, tag2), giftCertificateRepository.getAllTagsIdByCertificateId(1L));
        assertEquals(List.of(tag1, tag2), giftCertificateRepository.getAllTagsIdByCertificateId(2L));
    }


    @AfterEach
    public void drop() {
        embeddedDatabase.shutdown();
    }
}