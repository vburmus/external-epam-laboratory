package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateRepositoryTest {

    public static final BigDecimal PRICE = new BigDecimal("20.0");
    public static final int DURATION = 20;
    public static final String DESCRIPTION = "description";
    public static final String GIFT_CERTIFICATE_1 = "GiftCertificate1";
    public static final long ID = 1L;
    public static final String CREATE_DATE = "2023-01-15 00:36:20.0";
    public static final String LAST_UPDATE_DATE = "2023-01-16 01:20:09.0";
    public static final long ID1 = 2L;
    public static final String GIFT_CERTIFICATE_2 = "GiftCertificate2";
    public static final String CREATE_DATE1 = "2023-01-15 00:36:55.0";
    public static final String LAST_UPDATE_DATE1 = "2023-01-16 01:25:34.0";
    public static final String NAME = "Test1";
    public static final String NAME1 = "Tag1";
    public static final String NAME2 = "Tag2";
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

        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        assertTrue(giftCertificateRepository.createGiftCertificate(giftCertificate));

    }

    @Test
    void getAllGiftCertificates() {
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

        assertEquals(List.of(gc1, gc2), giftCertificateRepository.getAllGiftCertificates(1,10));
    }

    @Test
    void getGiftCertificateByID() {
        GiftCertificate gc1 = new GiftCertificate();
        gc1.setId(ID);
        gc1.setName(GIFT_CERTIFICATE_1);
        gc1.setDescription(DESCRIPTION);
        gc1.setDuration(DURATION);
        gc1.setPrice(PRICE);
        gc1.setCreateDate(CREATE_DATE);
        gc1.setLastUpdateDate(LAST_UPDATE_DATE);

        assertEquals(gc1, giftCertificateRepository.getGiftCertificateByID(ID));
    }

    @Test
    void isExistsGetIdAndDeleteGiftCertificate() {
        GiftCertificate gc1 = giftCertificateRepository.getGiftCertificateByID(1L);
        gc1.setId(ID);
        gc1.setName(GIFT_CERTIFICATE_1);
        gc1.setDescription(DESCRIPTION);
        gc1.setDuration(DURATION);
        gc1.setPrice(PRICE);
        gc1.setCreateDate(CREATE_DATE);
        gc1.setLastUpdateDate(LAST_UPDATE_DATE);
        assertTrue(giftCertificateRepository.isGiftCertificateExist(gc1));
        assertTrue(giftCertificateRepository.deleteGiftCertificate(giftCertificateRepository.getGiftCertificatesID(gc1)));
        assertFalse(giftCertificateRepository.isGiftCertificateExist(gc1));
    }

    @Test
    void updateGiftCertificate() {
        assertTrue(giftCertificateRepository.updateGiftCertificate(ID, Map.of("name", "newUpdate", "price", "222")));
        assertEquals("newUpdate", giftCertificateRepository.getGiftCertificateByID(ID).getName());
        assertEquals(new BigDecimal("222.0"), giftCertificateRepository.getGiftCertificateByID(ID).getPrice());
    }

    @Test
    void deleteTagDependenciesCreateGetAllByCertificate() {
        Tag tag1 = new Tag();
        tag1.setName(NAME1);
        tag1.setId(ID);
        Tag tag2 = new Tag();
        tag2.setName(NAME2);
        tag2.setId(ID1);
        assertEquals(List.of(tag1,tag2), giftCertificateRepository.getAllTagsIdByCertificateId(ID));
        assertEquals(List.of(tag2), giftCertificateRepository.getAllTagsIdByCertificateId(ID1));
        assertTrue(giftCertificateRepository.deleteTagDependenciesForGiftCertificate(List.of(tag1.getId(),tag2.getId()), ID));

        assertTrue(giftCertificateRepository.deleteTagDependenciesForGiftCertificate(List.of(tag2.getId()), ID1));
        assertEquals(List.of(), giftCertificateRepository.getAllTagsIdByCertificateId(ID));
        assertEquals(List.of(), giftCertificateRepository.getAllTagsIdByCertificateId(ID1));
        assertTrue(giftCertificateRepository.createTagDependenciesForGiftCertificate(List.of(tag1.getId(), tag2.getId()), ID));
        assertTrue(giftCertificateRepository.createTagDependenciesForGiftCertificate(List.of(tag1.getId(), tag2.getId()), ID1));
        assertEquals(List.of(tag1, tag2), giftCertificateRepository.getAllTagsIdByCertificateId(ID));
        assertEquals(List.of(tag1, tag2), giftCertificateRepository.getAllTagsIdByCertificateId(ID1));
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
        assertEquals(List.of(gc1), giftCertificateRepository.getGiftCertificatesByTagName("Tag1",1,10));
        assertEquals(List.of(gc1,gc2), giftCertificateRepository.getGiftCertificatesByPartOfName("Gift",1,10));
        assertEquals(List.of(gc1,gc2), giftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1), giftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.DESC,1,10));
        assertEquals(List.of(gc1,gc2), giftCertificateRepository.getGiftCertificatesByPartOfDescription("d",1,10));
        assertEquals(List.of(gc1,gc2), giftCertificateRepository.getCertificatesSortedByName(DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1), giftCertificateRepository.getCertificatesSortedByName(DirectionEnum.DESC,1,10));
        assertEquals
                (List.of(gc1,gc2), giftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc2,gc1), giftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.ASC,1,10));
        assertEquals(List.of(gc1,gc2), giftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.ASC,DirectionEnum.DESC,1,10));
        assertEquals(List.of(gc2,gc1), giftCertificateRepository.getCertificatesSortedByDateName(DirectionEnum.DESC,DirectionEnum.DESC,1,10));
    }

    @Test
    void getCertificatesBySeveralTags(){
        Tag tag1 = new Tag();
        tag1.setName(NAME1);
        tag1.setId(ID);
        Tag tag2 = new Tag();
        tag2.setName(NAME2);
        tag2.setId(ID1);
        GiftCertificate gc1 = new GiftCertificate();
        gc1.setId(ID);
        gc1.setName(GIFT_CERTIFICATE_1);
        gc1.setDescription(DESCRIPTION);
        gc1.setDuration(DURATION);
        gc1.setPrice(PRICE);
        gc1.setCreateDate(CREATE_DATE);
        gc1.setLastUpdateDate(LAST_UPDATE_DATE);
        assertEquals(List.of(gc1), giftCertificateRepository.getCertificatesBySeveralTags(List.of(tag1.getId(),tag2.getId()),1,10));
    }
    @Test
    void getCertificatesPriceByID(){
        assertEquals(PRICE, giftCertificateRepository.getCertificatesPriceByID(ID));
    }
    @AfterEach
    public void drop() {
        embeddedDatabase.shutdown();
    }
}