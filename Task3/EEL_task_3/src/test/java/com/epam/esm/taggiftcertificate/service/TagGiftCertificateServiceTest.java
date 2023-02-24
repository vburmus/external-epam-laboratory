package com.epam.esm.taggiftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.tag.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagGiftCertificateServiceTest {
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
    public static final int DURATION = 1;
    public static final int PRICE = 2;
    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMocked;
    @Mock
    private TagRepository tagRepositoryMocked;
    @Mock
    private TagService tagServiceMocked;
    @InjectMocks
    private GiftCertificateService giftCertificateService;


    @Test
    void getGiftCertificatesByTagNameObjectIsInvalid() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateService.getGiftCertificatesByTagName(null));
        assertEquals("Tag name " + null + " is invalid", thrown1.getMessage());
        ObjectIsInvalidException thrown2 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateService.getGiftCertificatesByTagName(""));
        assertEquals("Tag name " + " is invalid", thrown2.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameNoSuchItem() {
        when(tagServiceMocked.isTagWithNameExists(NAME)).thenReturn(false);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateService.getGiftCertificatesByTagName(NAME));
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
        when(giftCertificateRepositoryMocked.getGiftCertificatesByTagName(NAME)).thenReturn(List.of(gc, gc2));
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc)).thenReturn(ID1);
        when(giftCertificateRepositoryMocked.getGiftCertificatesID(gc2)).thenReturn(ID2);
        assertEquals(List.of(gc, gc2), giftCertificateService.getGiftCertificatesByTagName(NAME));
    }

    @Test
    void getGiftCertificatesByPart() {
    }

    @Test
    void getCertificatesSortedByDateDirectionException() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateService.getCertificatesSortedByDate(FAULT_DIRECTION));
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
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDate(DirectionEnum.ASC)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateService.getCertificatesSortedByDate(ASC));
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
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDate(DirectionEnum.DESC)).thenReturn(List.of(gc2, gc));
        assertEquals(List.of(gc2, gc), giftCertificateService.getCertificatesSortedByDate(DESC));

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

        when(giftCertificateRepositoryMocked.getCertificatesSortedByName(DirectionEnum.ASC)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateService.getCertificatesSortedByName(ASC));
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
        when(giftCertificateRepositoryMocked.getCertificatesSortedByName(DirectionEnum.DESC)).thenReturn(List.of(gc2, gc));
        assertEquals(List.of(gc2, gc), giftCertificateService.getCertificatesSortedByName(DESC));

    }

    @Test
    void getCertificatesSortedByNameDirectionException() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateService.getCertificatesSortedByName(FAULT_DIRECTION));
        assertEquals("Direction " + FAULT_DIRECTION + " is invalid", thrown1.getMessage());

    }

    @Test
    void getCertificatesSortedByDateNameDirectionInvalid() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateService.getCertificatesSortedByDateName(FAULT_DIRECTION, FAULT_DIRECTION));
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
        when(giftCertificateRepositoryMocked.getCertificatesSortedByDateName(DirectionEnum.ASC, DirectionEnum.ASC)).thenReturn(List.of(gc, gc2));
        assertEquals(List.of(gc, gc2), giftCertificateService.getCertificatesSortedByDateName(ASC, ASC));

    }
}