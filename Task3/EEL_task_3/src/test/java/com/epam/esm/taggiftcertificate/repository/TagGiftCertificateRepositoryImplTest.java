package com.epam.esm.taggiftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepositoryImpl;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagGiftCertificateRepositoryImplTest {
    private GiftCertificateRepository tagGiftCertificateRepository;
    private EmbeddedDatabase embeddedDatabase;

    @BeforeEach
    public void init() {
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embeddedDB/task_3.sql")
                .addScript("embeddedDB/insert-some-data.sql")
                .build();
        this.tagGiftCertificateRepository = new GiftCertificateRepositoryImpl(new JdbcTemplate(embeddedDatabase));
    }


    @Test
    void allSortingAndGettingMethods() {
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
        assertEquals(List.of(gc1),tagGiftCertificateRepository.getGiftCertificatesByTagName("Tag1"));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getGiftCertificatesByPartOfName("Gift"));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.ASC));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.DESC));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getGiftCertificatesByPartOfDescription("d"));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.ASC));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.DESC));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.ASC));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.ASC));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.DESC));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.DESC));


    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}