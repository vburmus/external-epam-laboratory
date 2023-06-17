package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exceptions.rest.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.rest.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.rest.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.model.TagDTO;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import com.epam.esm.utils.mappers.EntityToDtoMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.Constants.*;
import static com.epam.esm.utils.Constants.THERE_IS_NO_GC_WITH_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {


    private final EntityToDtoMapper entityMapper = new EntityToDtoMapperImpl();
    @Mock
    private EntityToDtoMapper entityToDtoMapper;
    @Mock
    private TagRepository tagRepositoryMocked;
    @InjectMocks
    private TagService tagServiceMocked;

    @Test
    void createTagObjectAlreadyExists() {
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        TagDTO tagDTO = entityMapper.toTagDTO(tag);
        when(entityToDtoMapper.toTag(tagDTO)).thenReturn(tag);
        when(tagRepositoryMocked.exists(Example.of(tag))).thenReturn(true);
        ObjectAlreadyExistsException thrown = assertThrows(ObjectAlreadyExistsException.class,
                () -> tagServiceMocked.createTag(tagDTO));
        assertEquals("Tag with name = " + TEST_TAG + " already exists", thrown.getMessage());

    }

    @Test
    void createTagIsInvalid() {
        Tag tag = new Tag();
        TagDTO tagDTO = entityMapper.toTagDTO(tag);
        when(entityToDtoMapper.toTag(tagDTO)).thenReturn(tag);
        when(tagRepositoryMocked.exists(Example.of(tag))).thenReturn(false);
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> tagServiceMocked.createTag(tagDTO));
        assertEquals("Tag with name = " + null + " is invalid", thrown.getMessage());

    }

    @Test
    void createTagSuccess() {
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        TagDTO tagDTO = entityMapper.toTagDTO(tag);
        when(entityToDtoMapper.toTag(tagDTO)).thenReturn(tag);
        when(tagRepositoryMocked.exists(Example.of(tag))).thenReturn(false);
        when(tagRepositoryMocked.save(tag)).thenReturn(tag);
        when(entityToDtoMapper.toTagDTO(tag)).thenReturn(tagDTO);
        assertEquals(tagDTO, tagServiceMocked.createTag(tagDTO));

    }


    @Test
    void getAllTagsSuccess() {
        List<Tag> tags = List.of(new Tag(), new Tag(), new Tag());
        Page<Tag> tagsPage = new PageImpl<>(tags, PageRequest.of(0, 10), tags.size());
        when(tagRepositoryMocked.findAll(PageRequest.of(0, 10))).thenReturn(tagsPage);
        assertEquals(tagsPage.map(entityToDtoMapper::toTagDTO), tagServiceMocked.getAllTags(1, 10));
    }

    @Test
    void getTagByIdNoSuchItem() {
        when(tagRepositoryMocked.findById(ID1)).thenReturn(Optional.empty());
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.getTagById(ID1));
        assertEquals("Tag with id = " + ID1 + "doesn't exist", thrown.getMessage());

    }

    @Test
    void getTagByIdSuccess() {
        Tag tag = Tag.builder().id(ID1).name(TAG_1).build();
        TagDTO tagDTO = entityMapper.toTagDTO(tag);
        when(tagRepositoryMocked.findById(ID1)).thenReturn(Optional.of(tag));
        when(entityToDtoMapper.toTagDTO(tag)).thenReturn(tagDTO);
        assertEquals(entityMapper.toTagDTO(tag), tagServiceMocked.getTagById(ID1));
    }

    @Test
    void deleteTagSuccess() {
        when(tagRepositoryMocked.existsById(ID1)).thenReturn(true);
        doNothing().when(tagRepositoryMocked).deleteById(ID1);
        assertTrue(tagServiceMocked.deleteTag(ID1));
    }

    @Test
    void deleteTagNoSuchItem() {
        when(tagRepositoryMocked.existsById(ID1)).thenReturn(false);

        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.deleteTag(ID1));
        assertEquals(THERE_IS_NO_GC_WITH_ID + ID1, thrown.getMessage());

    }


    @Test
    void existsByName() {
        when(tagRepositoryMocked.existsByName(TAG_1)).thenReturn(true);
        assertTrue(tagServiceMocked.existsByName(TAG_1));
    }

    @Test
    void getMostUsedTag() {
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        when(tagRepositoryMocked.getMostUsedTag()).thenReturn(Optional.of(tag));
        TagDTO tagDTO = entityMapper.toTagDTO(tag);
        when(entityToDtoMapper.toTagDTO(tag)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagServiceMocked.getMostUsedTag());
    }

    @Test
    void getMostUsedTagNoSuchItemError() {
        when(tagRepositoryMocked.getMostUsedTag()).thenReturn(Optional.empty());

        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.getMostUsedTag());
        assertEquals("There is no tags in this purchase", thrown.getMessage());

    }

    @Test
    void checkTagsAndSave() {
        Tag tag1 = Tag.builder().name(TAG_1).build();
        Tag tag2 = Tag.builder().name(TAG_2).build();
        List<Tag> tags = List.of(tag1, tag2);
        GiftCertificate gc = GiftCertificate.builder().tags(tags).build();
        when(tagRepositoryMocked.exists(Example.of(tag1))).thenReturn(true);
        when(tagRepositoryMocked.findOne(Example.of(tag1))).thenReturn(Optional.of(tag1));
        when(tagRepositoryMocked.exists(Example.of(tag2))).thenReturn(false);
        when(tagRepositoryMocked.save(tag2)).thenReturn(tag2);
        assertEquals(tags, tagServiceMocked.checkTagsAndSaveIfDontExist(gc));
    }

    @Test
    void checkAndSaveEmptyList() {
        assertEquals(Collections.emptyList(), tagServiceMocked.checkTagsAndSaveIfDontExist(new GiftCertificate()));
    }

}
