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
    public static final long ID = 1L;
    public static final String GIFT_CERTIFICATE_1 = "GiftCertificate1";
    public static final String DESCRIPTION = "description";
    public static final int DURATION = 20;
    public static final double PRICE = 20.0;
    public static final String CREATE_DATE = "2023-01-15 00:36:20.0";
    public static final String LAST_UPDATE_DATE = "2023-01-16 01:20:09.0";
    public static final long ID1 = 2L;
    public static final String GIFT_CERTIFICATE_2 = "GiftCertificate2";
    public static final String CREATE_DATE1 = "2023-01-15 00:36:55.0";
    public static final String LAST_UPDATE_DATE1 = "2023-01-16 01:25:34.0";
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
        gc1.setId(ID);
        gc1.setName(GIFT_CERTIFICATE_1);
        gc1.setDescription(DESCRIPTION);
        gc1.setDuration(DURATION);
        gc1.setPrice(PRICE);
        gc1.setCreateDate(CREATE_DATE);
        gc1.setLastUpdateDate(LAST_UPDATE_DATE);

        GiftCertificate gc2 = new GiftCertificate();
        gc2.setId(ID1);
        gc2.setName(GIFT_CERTIFICATE_2);
        gc2.setDescription(DESCRIPTION);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setCreateDate(CREATE_DATE1);
        gc2.setLastUpdateDate(LAST_UPDATE_DATE1);
        assertEquals(List.of(gc1),tagGiftCertificateRepository.getGiftCertificatesByTagName("Tag1",1,10));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getGiftCertificatesByPartOfName("Gift",1,10));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.DESC,1,10));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getGiftCertificatesByPartOfDescription("d",1,10));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.DESC,1,10));
        assertEquals
                (List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc1,gc2),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.DESC,1,10));
        assertEquals(List.of(gc2,gc1),tagGiftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.DESC,1,10));


    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}