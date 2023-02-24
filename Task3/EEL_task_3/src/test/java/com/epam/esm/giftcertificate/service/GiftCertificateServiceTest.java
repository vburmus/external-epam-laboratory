package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;

import com.epam.esm.tag.service.TagService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {
    final static long ID = 1L;
    public static final String TEST_TAG = "TestTag";
    public static final String TEST_CERT = "Test";
    public static final int DURATION = 1;
    public static final int PRICE = 2;
    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMocked;

    @Mock
    private TagService tagServiceMocked;

    @InjectMocks
    private GiftCertificateService giftCertificateServiceMocked;

    @Test
    void createCertificateSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID);
        gc.setId(ID);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));

        when(giftCertificateRepositoryMocked.isGiftCertificateExist(gc)).thenReturn(false);
        when(giftCertificateRepositoryMocked.createGiftCertificate(gc)).thenReturn(true);

        when(tagServiceMocked.getTagsIds(gc.getTags())).thenReturn(List.of(ID));

        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(gc.getId());
        when(giftCertificateRepositoryMocked.createTagDependenciesForGiftCertificate(List.of(ID), ID)).thenReturn(true);
        when(giftCertificateServiceMocked.getAllTagsByCertificate(gc)).thenReturn(gc.getTags());
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(gc.getId());
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID)).thenReturn(gc);
        assertEquals(gc,giftCertificateServiceMocked.createCertificate(gc));
    }
    @Test
    void createCertificateIsInvalid() {
        GiftCertificate gc = new GiftCertificate();
        gc.setId(ID);
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
        t.setId(ID);
        gc.setId(ID);
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
        t.setId(ID);
        gc.setId(ID);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.deleteGiftCertificate(ID)).thenReturn(true);
        assertTrue(giftCertificateServiceMocked.deleteCertificate(ID));

    }
    @Test
    void deleteCertificateFalse() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID);
        gc.setId(ID);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.deleteGiftCertificate(gc.getId())).thenReturn(false);

        assertFalse(giftCertificateServiceMocked.deleteCertificate(ID));

    }


    @Test
    void updateCertificateObjectIsInvalidCertificate() {
        GiftCertificate gc = new GiftCertificate();
         ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.updateCertificate(ID,gc));
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
                () -> giftCertificateServiceMocked.updateCertificate(ID,gc));
        assertEquals("Some tags are invalid" , thrown.getMessage());

    }
    @Test
    void updateCertificateWithNoTags() {
        GiftCertificate gc = new GiftCertificate();
        gc.setId(ID);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        when(giftCertificateServiceMocked.updateCertificate(ID,gc)).thenReturn(true);
        assertTrue(giftCertificateServiceMocked.updateCertificate(ID,gc));

    }

    @Test
    void getCertificateByIdSuccess() {
        GiftCertificate gc = new GiftCertificate();
        Tag t = new Tag();
        t.setName(TEST_TAG);
        t.setId(ID);
        gc.setId(ID);
        gc.setName(TEST_CERT);
        gc.setDuration(DURATION);
        gc.setPrice(PRICE);
        gc.setDescription(TEST_CERT);
        gc.setTags(List.of(t));
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID)).thenReturn(gc);
        assertEquals(gc,giftCertificateServiceMocked.getCertificateById(ID));

    }
    @Test
    void getCertificateByIdNoSuchItem() {
        when(giftCertificateRepositoryMocked.getGiftCertificateByID(ID)).thenReturn(null);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getCertificateById(ID));
        assertEquals("GiftCertificate with id = " + ID + " doesn't exist", thrown.getMessage());

    }
}