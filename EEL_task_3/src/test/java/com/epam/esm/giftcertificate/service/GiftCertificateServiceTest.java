package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.Constants;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import com.epam.esm.utils.mappers.EntityToDtoMapperImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.Constants.*;

import static com.epam.esm.utils.Constants.CREATE_DATE;
import static com.epam.esm.utils.Constants.LAST_UPDATE_DATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceTest {


    private final EntityToDtoMapper entityMapper = new EntityToDtoMapperImpl();
    @Mock
    private GiftCertificateRepository giftCertificateRepositoryMocked;
    @Mock
    private TagService tagServiceMocked;
    @Mock
    private EntityToDtoMapper entityToDtoMapper;
    @InjectMocks
    private GiftCertificateService giftCertificateServiceMocked;

    @Test
    void createCertificateSuccess() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();
        GiftCertificateDTO giftCertificateDTO = entityMapper.toGiftCertificateDTO(gc);


        when(entityToDtoMapper.toGiftCertificate(giftCertificateDTO)).thenReturn(gc);
        when(giftCertificateRepositoryMocked.exists(getGiftCertificateExample(gc))).thenReturn(false);
        when(giftCertificateRepositoryMocked.save(gc)).thenReturn(gc);
        when(tagServiceMocked.checkTagsAndSaveIfDontExist(gc)).thenReturn(gc.getTags());
        when(giftCertificateRepositoryMocked.save(gc)).thenReturn(gc);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(giftCertificateDTO);

        assertEquals(giftCertificateDTO, giftCertificateServiceMocked.createCertificate(giftCertificateDTO));
    }

    @Test
    void createCertificateIsInvalid() {
        GiftCertificateDTO gcDTO = new GiftCertificateDTO();
        gcDTO.setId(ID1);
        gcDTO.setName(TEST_CERT);
        gcDTO.setDuration(DURATION_VAL);
        GiftCertificate gc = entityMapper.toGiftCertificate(gcDTO);

        when(entityToDtoMapper.toGiftCertificate(gcDTO)).thenReturn(gc);

        when(giftCertificateRepositoryMocked.exists(getGiftCertificateExample(gc))).thenReturn(false);
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.createCertificate(gcDTO));
        assertEquals("Gift certificate with name = " + TEST_CERT + ", duration = " + DURATION_VAL + " is invalid, please check your params",
                thrown.getMessage());
    }

    @Test
    void createCertificateAlreadyExists() {
        GiftCertificateDTO gcDTO = new GiftCertificateDTO();
        GiftCertificate gc = entityMapper.toGiftCertificate(gcDTO);
        when(entityToDtoMapper.toGiftCertificate(gcDTO)).thenReturn(gc);
        when(giftCertificateRepositoryMocked.exists(getGiftCertificateExample(gc))).thenReturn(true);
        ObjectAlreadyExistsException thrown = assertThrows(ObjectAlreadyExistsException.class,
                () -> giftCertificateServiceMocked.createCertificate(gcDTO));
        assertEquals("Gift certificate with name = " + null + ", duration = " + null + " already exists", thrown.getMessage());

    }

    private Example<GiftCertificate> getGiftCertificateExample(GiftCertificate gc) {
        ExampleMatcher gcMatcher = ExampleMatcher.matching()
                .withIgnorePaths(CREATE_DATE, LAST_UPDATE_DATE, ID, TAGS, Constants.PRICE)
                .withMatcher(NAME, exact())
                .withMatcher(Constants.DESCRIPTION, exact())
                .withMatcher(Constants.DURATION, exact());
        return Example.of(gc, gcMatcher);
    }

    @Test
    void deleteCertificateSuccess() {
        when(giftCertificateRepositoryMocked.existsById(ID1)).thenReturn(true);
        doNothing().when(giftCertificateRepositoryMocked).deleteById(ID1);
        assertTrue(giftCertificateServiceMocked.deleteCertificate(ID1));

    }

    @Test
    void deleteCertificateError() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();
        when(giftCertificateRepositoryMocked.existsById(gc.getId())).thenReturn(false);
        NoSuchItemException e = assertThrows(NoSuchItemException.class, () -> giftCertificateServiceMocked.deleteCertificate(ID1));
        assertEquals("There is no gc with id= " + ID1, e.getMessage());
    }


    @Test
    void updateCertificateObjectIsInvalidCertificate() {
        when(giftCertificateRepositoryMocked.findById(ID1)).thenReturn(Optional.empty());
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.updateCertificate(ID1, null));
        assertEquals("No gift certificate with id = " + ID1, thrown.getMessage());

    }

    @Test
    void updateCertificateSuccess() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();
        when(giftCertificateRepositoryMocked.findById(ID1)).thenReturn(Optional.of(gc));

        when(tagServiceMocked.checkTagsAndSaveIfDontExist(gc)).thenReturn(gc.getTags());
        when(giftCertificateRepositoryMocked.save(gc)).thenReturn(gc);
        GiftCertificateDTO gcDTO = entityMapper.toGiftCertificateDTO(gc);
        gc.setName(NEW);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(gcDTO);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonMergePatch jsonMergePatch = JsonMergePatch.fromJson(objectMapper.convertValue(gc, JsonNode.class));
            assertEquals(gcDTO, giftCertificateServiceMocked.updateCertificate(ID1, jsonMergePatch));
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void getCertificateByIdSuccess() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();
        when(giftCertificateRepositoryMocked.findById(ID1)).thenReturn(Optional.of(gc));
        GiftCertificateDTO gcDTO = entityMapper.toGiftCertificateDTO(gc);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(gcDTO);
        assertEquals(gcDTO, giftCertificateServiceMocked.getCertificateById(ID1));

    }

    @Test
    void getCertificateByIdNoSuchItem() {
        when(giftCertificateRepositoryMocked.findById(ID1)).thenReturn(Optional.empty());
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getCertificateById(ID1));
        assertEquals("GiftCertificate with id = " + ID1 + " doesn't exist", thrown.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameObjectIsInvalid() {
        ObjectIsInvalidException thrown1 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName(null, 1, 10));
        assertEquals("Tag name " + null + " is invalid", thrown1.getMessage());
        ObjectIsInvalidException thrown2 = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName("", 1, 10));
        assertEquals("Tag name " + " is invalid", thrown2.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameNoSuchItem() {
        when(tagServiceMocked.existsByName(NAME)).thenReturn(false);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByTagName(NAME, 1, 10));
        assertEquals("Tag with name " + NAME + " doesn't exist", thrown.getMessage());

    }

    @Test
    void getGiftCertificatesByTagNameSuccess() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();

        Tag t2 = Tag.builder()
                .id(ID2)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc2 = GiftCertificate.builder()
                .id(ID2)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t2))
                .build();

        when(tagServiceMocked.existsByName(NAME)).thenReturn(true);
        List<GiftCertificate> gcList = List.of(gc, gc2);
        Page<GiftCertificate> page = new PageImpl<>(gcList,
                PageRequest.of(0, 10), gcList.size());
        when(giftCertificateRepositoryMocked.findByTagsName(NAME, PageRequest.of(0, 10))).thenReturn(page);
        GiftCertificateDTO gcDTO = entityMapper.toGiftCertificateDTO(gc);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(gcDTO);
        GiftCertificateDTO gcDTO2 = entityMapper.toGiftCertificateDTO(gc2);
        when(entityToDtoMapper.toGiftCertificateDTO(gc2)).thenReturn(gcDTO2);
        List<GiftCertificateDTO> dtoList = List.of(gcDTO, gcDTO2);
        Page<GiftCertificateDTO> pageDTO = new PageImpl<>(dtoList,
                PageRequest.of(0, 10), gcList.size());
        assertEquals(pageDTO, giftCertificateServiceMocked.getGiftCertificatesByTagName(NAME, 1,
                10));
    }

    @Test
    void getCertificatesSortedThreeParamsError() {
        ObjectIsInvalidException oie = assertThrows(ObjectIsInvalidException.class, () ->
                giftCertificateServiceMocked.getCertificatesSortedByParam(WRONG_SORT_PARAM, 1, 10));
        assertEquals("To many params for sorting", oie.getMessage());
    }

    @Test
    void getCertificatesSortedParamNamingError() {
        ObjectIsInvalidException oie = assertThrows(ObjectIsInvalidException.class, () ->
                giftCertificateServiceMocked.getCertificatesSortedByParam(WRONG_SORT_PARAM_2, 1, 10));
        assertEquals("Some sort params are invalid.", oie.getMessage());
    }

    @Test
    void getCertificatesSortedByDateASCSuccess() {
        Tag t = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t))
                .build();

        Tag t2 = Tag.builder()
                .id(ID2)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc2 = GiftCertificate.builder()
                .id(ID2)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(Collections.singletonList(t2))
                .build();
        GiftCertificateDTO gcDTO = entityMapper.toGiftCertificateDTO(gc);

        GiftCertificateDTO gcDTO2 = entityMapper.toGiftCertificateDTO(gc2);

        List<GiftCertificate> gcList = List.of(gc, gc2);
        List<GiftCertificateDTO> dtoList = List.of(gcDTO, gcDTO2);
        Page<GiftCertificateDTO> pageDTO =
                new PageImpl<>(dtoList,
                        PageRequest.of(0, 10), dtoList.size());
        Page<GiftCertificate> page =
                new PageImpl<>(gcList,
                        PageRequest.of(0, 10), gcList.size());
        Sort sortBy = Sort.by(Sort.Direction.ASC, "createDate");
        PageRequest pageRequest = PageRequest.of(0, 10, sortBy);
        when(giftCertificateRepositoryMocked.findAll(pageRequest)).thenReturn(page);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(gcDTO);
        when(entityToDtoMapper.toGiftCertificateDTO(gc2)).thenReturn(gcDTO2);
        assertEquals(pageDTO, giftCertificateServiceMocked.getCertificatesSortedByParam("date", 1, 10));
        sortBy = Sort.by(new Sort.Order(Sort.Direction.ASC, "name"), new Sort.Order(Sort.Direction.ASC, "createDate"));
        pageRequest = PageRequest.of(0, 10, sortBy);
        when(giftCertificateRepositoryMocked.findAll(pageRequest)).thenReturn(page);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(gcDTO);
        when(entityToDtoMapper.toGiftCertificateDTO(gc2)).thenReturn(gcDTO2);
        assertEquals(pageDTO, giftCertificateServiceMocked.getCertificatesSortedByParam("name,date", 1, 10));

    }

    @Test
    void getGCBySeveralTagsSuccess() {
        Tag t1 = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        Tag t2 = Tag.builder()
                .id(ID2)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(List.of(t1, t2))
                .build();


        List<GiftCertificate> res = List.of(gc);
        Page<GiftCertificate> page = new PageImpl<>(res, PageRequest.of(0, 10), res.size());
        when(giftCertificateRepositoryMocked.findByTagsIdIn(List.of(ID1, ID2), PageRequest.of(0, 10))).thenReturn(page);
        GiftCertificateDTO dtoGC = entityMapper.toGiftCertificateDTO(gc);
        List<GiftCertificateDTO> resDTO = List.of(dtoGC);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(dtoGC);
        Page<GiftCertificateDTO> pageDTO = new PageImpl<>(resDTO, PageRequest.of(0, 10), resDTO.size());

        assertEquals(pageDTO, giftCertificateServiceMocked.getCertificatesBySeveralTags(List.of(t1.getId(), t2.getId()),
                1, 10));
    }

    @Test
    void getByPartInvalid() {
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByPart("", 1, 10));
        assertEquals("Part of description ->  is invalid", thrown.getMessage());
    }

    @Test
    void getByPartEmptyResult() {
        when(giftCertificateRepositoryMocked.findByNameContaining("part", PageRequest.of(0, 10))).thenReturn(Page.empty(PageRequest.of(0,
                10)));
        when(giftCertificateRepositoryMocked.findByDescriptionContaining("part", PageRequest.of(0, 10))).thenReturn(Page.empty(PageRequest.of(0,
                10)));
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getGiftCertificatesByPart("part", 1, 10));
        assertEquals("List is empty!", thrown.getMessage());
    }

    @Test
    void getByPartNameDescriptionSuccess() {
        Tag t1 = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        Tag t2 = Tag.builder()
                .id(ID2)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(List.of(t1, t2))
                .build();

        List<GiftCertificate> res = List.of(gc);
        Page<GiftCertificate> page = new PageImpl<>(res, PageRequest.of(0, 10), res.size());
        GiftCertificateDTO dtoGC = entityMapper.toGiftCertificateDTO(gc);
        List<GiftCertificateDTO> resDTO = List.of(dtoGC);
        Page<GiftCertificateDTO> pageDTO = new PageImpl<>(resDTO, PageRequest.of(0, 10), resDTO.size());

        when(giftCertificateRepositoryMocked.findByNameContaining("part", PageRequest.of(0, 10))).thenReturn(page);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(dtoGC);

        assertEquals(pageDTO, giftCertificateServiceMocked.getGiftCertificatesByPart("part", 1, 10));
        when(giftCertificateRepositoryMocked.findByNameContaining("part", PageRequest.of(0, 10))).thenReturn(Page.empty(PageRequest.of(0,
                10)));
        when(giftCertificateRepositoryMocked.findByDescriptionContaining("part", PageRequest.of(0, 10))).thenReturn(page);
        assertEquals(pageDTO, giftCertificateServiceMocked.getGiftCertificatesByPart("part", 1, 10));

    }

    @Test
    void getAllEmptyError() {
        when(giftCertificateRepositoryMocked.findAll(PageRequest.of(0, 10))).thenReturn(Page.empty(PageRequest.of(0,
                10)));

        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> giftCertificateServiceMocked.getAllGiftCertificates(1, 10));
        assertEquals("List is empty!", thrown.getMessage());
    }

    @Test
    void getAllSuccess() {
        Tag t1 = Tag.builder()
                .id(ID1)
                .name(TEST_TAG)
                .build();

        Tag t2 = Tag.builder()
                .id(ID2)
                .name(TEST_TAG)
                .build();

        GiftCertificate gc = GiftCertificate.builder()
                .id(ID1)
                .name(TEST_CERT)
                .duration(DURATION_VAL)
                .price(PRICE)
                .description(TEST_CERT)
                .tags(List.of(t1, t2))
                .build();

        List<GiftCertificate> res = List.of(gc);
        Page<GiftCertificate> page = new PageImpl<>(res, PageRequest.of(0, 10), res.size());
        GiftCertificateDTO dtoGC = entityMapper.toGiftCertificateDTO(gc);
        List<GiftCertificateDTO> resDTO = List.of(dtoGC);
        Page<GiftCertificateDTO> pageDTO = new PageImpl<>(resDTO, PageRequest.of(0, 10), resDTO.size());
        when(giftCertificateRepositoryMocked.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(entityToDtoMapper.toGiftCertificateDTO(gc)).thenReturn(dtoGC);
        assertEquals(pageDTO, giftCertificateServiceMocked.getAllGiftCertificates(1, 10));
    }
}
