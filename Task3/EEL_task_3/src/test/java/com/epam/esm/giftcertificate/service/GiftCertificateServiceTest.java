package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;

import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.tag.service.TagService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {
    public static final String TEST_TAG = "TestTag";
    public static final String TEST_CERT = "Test";
    public static final int DURATION = 1;
    public static final BigDecimal PRICE = new BigDecimal("20.0");
    public final static String NAME = "name";
    public static final long ID1 = 1L;
    public static final long ID2 = 2L;
    public static final String FAULT_DIRECTION = "A";
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    public static final String TEST_TAG_1 = "TestTag1";
    public static final String TEST_CERT_1 = "Test1";
    public static final String DESCRIPTION = "...";
    public static final String TEST_CERT_2 = "Test2";
    public static final String TEST_TAG_2 = "TestTag2";
    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMocked;

    @Mock
    private TagService tagServiceMocked;

    @Mock
    private TagRepository tagRepositoryMocked;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMocked;

    @Test
    void createCertificateSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));

        when(giftCertificateRepositoryMocked.isGiftCertificateExist(gc)).thenReturn(false);
        when(giftCertificateRepositoryMocked.createGiftCertificate(gc)).thenReturn(true);

        when(tagServiceMocked.getTagsIds(gc.getTags())).thenReturn(List.of(ID1));

        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(gc.getId());
        when(giftCertificateRepositoryMocked.createTagDependenciesForGiftCertificate(List.of(ID1), ID1)).thenReturn(true);
        when(giftCertificateServiceMocked.getAllTagsByCertificate(gc)).thenReturn(gc.getTags());
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(gc.getId());
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID1)).thenReturn(gc);
        assertEquals(gc,giftCertificateServiceMocked.createCertificate(gc));
    }
    @Test
    void createCertificateIsInvalid() {
        GiftCertificate gc = new GiftCertificate();
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        when(giftCertificateRepositoryMocked.isGiftCertificateExist(gc)).thenReturn(false);
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.createCertificate(gc));
        assertEquals("Gift certificate with name = " +  TEST_CERT + ", duration = " + DURATION + "is invalid, please check your params", thrown.getMessage());
    }
    @Test
    void createCertificateAlreadyExists() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.isGiftCertificateExist(gc)).thenReturn(true);
        ObjectAlreadyExistsException thrown = assertThrows(ObjectAlreadyExistsException.class,
                () -> giftCertificateServiceMocked.createCertificate(gc));
        assertEquals("Gift certificate with name = " +  TEST_CERT + ", duration = " + DURATION + " already exists", thrown.getMessage());

    }
    @Test
    void deleteCertificateSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.deleteGiftCertificate(ID1)).thenReturn(true);
        assertTrue(giftCertificateServiceMocked.deleteCertificate(ID1));

    }
    @Test
    void deleteCertificateFalse() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.deleteGiftCertificate(gc.getId())).thenReturn(false);

        assertFalse(giftCertificateServiceMocked.deleteCertificate(ID1));

    }


    @Test
    void updateCertificateObjectIsInvalidCertificate() {
        GiftCertificate gc = new GiftCertificate();
         ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.updateCertificate(ID1,gc));
        assertEquals("Please check params for certificate name = " +  null , thrown.getMessage());

    }
    @Test
    void updateCertificateObjectIsInvalidTag() {
        GiftCertificate gc = new GiftCertificate();

        gc.setId(1L);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        Tag t = new Tag();
        gc.setTags(List.of(t));
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.updateCertificate(ID1,gc));
        assertEquals("Some tags are invalid" , thrown.getMessage());

    }
    @Test
    void updateCertificateWithNoTags() {
        GiftCertificate gc = new GiftCertificate();
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        when(giftCertificateServiceMocked.updateCertificate(ID1,gc)).thenReturn(true);
        assertTrue(giftCertificateServiceMocked.updateCertificate(ID1,gc));

    }

    @Test
    void getCertificateByIdSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID1)).thenReturn(gc);
        assertEquals(gc,giftCertificateServiceMocked.getCertificateById(ID1));

    }
    @Test
    void getCertificateByIdNoSuchItem() {
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID1)).thenReturn(null);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getCertificateById(ID1));
        assertEquals("GiftCertificate with id = " + ID1 + " doesn't exist", thrown.getMessage());

    }
    @Test
    void getGiftCertificatesByTagNameObjectIsInvalid() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName(null,1,10));
        assertEquals("Tag name " + null + " is invalid", thrown1.getMessage());
        ObjectIsInvalidException thrown2 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName("",1,10));
        assertEquals("Tag name " + " is invalid", thrown2.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameNoSuchItem() {
        when(tagServiceMocked.isTagWithNameExists(NAME)).thenReturn(false);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName(NAME,1,10));
        assertEquals("Tag with name " + NAME + " doesn't exist", thrown.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT_1);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));
        when(tagServiceMocked.isTagWithNameExists(NAME)).thenReturn(true);
        when(giftCertificateRepositoryMocked.getGiftCertificatesByTagName(NAME,1,10)).thenReturn(List.of(gc, gc2));
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(ID1);
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc2)).thenReturn(ID2);
        assertEquals(List.of(gc, gc2), giftCertificateServiceMocked.getGiftCertificatesByTagName(NAME,1,10));
    }


    @Test
    void getCertificatesSortedByDateDirectionException() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getCertificatesSortedByDate(FAULT_DIRECTION,1,10));
        assertEquals("Direction " + FAULT_DIRECTION + " is invalid", thrown1.getMessage());

    }

    @Test
    void getCertificatesSortedByDateASC() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDate(DirectionEnum.ASC,1,10)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateServiceMocked.getCertificatesSortedByDate(ASC,1,10));
    }

    @Test
    void getCertificatesSortedByDateDESC() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDate(DirectionEnum.DESC,1,10)).thenReturn(List.of(gc2, gc));
        assertEquals(List.of(gc2, gc), giftCertificateServiceMocked.getCertificatesSortedByDate(DESC,1,10));

    }

    @Test
    void getCertificatesSortedByNameASC() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));

        when(giftCertificateRepositoryMocked.getCertificatesSortedByName(DirectionEnum.ASC,1,10)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateServiceMocked.getCertificatesSortedByName(ASC,1,10));
    }

    @Test
    void getCertificatesSortedByNameDESC() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));
        when(giftCertificateRepositoryMocked.getCertificatesSortedByName(DirectionEnum.DESC,1,10)).thenReturn(List.of(gc2, gc));
        assertEquals(List.of(gc2, gc), giftCertificateServiceMocked.getCertificatesSortedByName(DESC,1,10));

    }

    @Test
    void getCertificatesSortedByNameDirectionException() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getCertificatesSortedByName(FAULT_DIRECTION,1,10));
        assertEquals("Direction " + FAULT_DIRECTION + " is invalid", thrown1.getMessage());

    }

    @Test
    void getCertificatesSortedByDateNameDirectionInvalid() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getCertificatesSortedByDateName(FAULT_DIRECTION, FAULT_DIRECTION,1,10));
        assertEquals("Some of directions: " + FAULT_DIRECTION + " " + FAULT_DIRECTION+ " are invalid", thrown1.getMessage());

    }

    @Test
    void getCertificatesSortedByDateNameSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG_1);
        t.setId(ID1);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t));

        GiftCertificate gc2 = new GiftCertificate();
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_2);
        t2.setId(ID2);
        gc2.setId(ID2);
        gc2.setName(TEST_CERT_2);
        gc2.setDuration(DURATION);
        gc2.setPrice(PRICE);
        gc2.setDescription(DESCRIPTION);
        gc2.setTags(List.of(t2));
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(ID1);
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc2)).thenReturn(ID2);
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDateName(DirectionEnum.ASC, DirectionEnum.ASC,1,10)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateServiceMocked.getCertificatesSortedByDateName(ASC, ASC,1,10));

    }
    @Test
    void getGCBySeveralTags(){
        GiftCertificate gc = new GiftCertificate();
        Tag t1 = new Tag();
        t1.setName(TEST_TAG_1);
        t1.setId(ID1);
        Tag t2 = new Tag();
        t2.setName(TEST_TAG_1);
        t2.setId(ID2);
        gc.setId(ID1);
        gc.setName(TEST_CERT_1);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(DESCRIPTION);
        gc.setTags(List.of(t1,t2));
        when(giftCertificateRepositoryMocked.getCertificatesBySeveralTags(List.of(ID1,ID2),1,10)).thenReturn(Collections.singletonList(gc));

        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(ID1);
        when(giftCertificateServiceMocked.setTagsInCertificates(List.of(gc))).thenReturn(List.of(gc));
        assertEquals(List.of(gc),giftCertificateServiceMocked.getCertificatesBySeveralTags(List.of(t1.getId(),t2.getId()),1,10));
    }
}