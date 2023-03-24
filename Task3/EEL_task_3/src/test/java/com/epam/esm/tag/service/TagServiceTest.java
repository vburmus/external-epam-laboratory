package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    public static final String TEST_TAG = "TestTag";
    public static final long ID1 = 1L;
    public static final long ID2 = 2L;
    public static final long ID3 = 3L;
    @Mock
    private TagRepository tagRepositoryMocked;
    @InjectMocks
    private TagService tagServiceMocked;

    @Test
    void createTagObjectAlreadyExists() {
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        when(tagRepositoryMocked.isTagExists(TEST_TAG)).thenReturn(true);
        ObjectAlreadyExistsException thrown = assertThrows(ObjectAlreadyExistsException.class,
                () -> tagServiceMocked.createTag(tag));
        assertEquals("Tag with name = " + TEST_TAG + " already exists", thrown.getMessage());

    }
    @Test
    void createTagIsInvalid() {
        Tag tag = new Tag();

        when(tagRepositoryMocked.isTagExists(null)).thenReturn(false);
        ObjectIsInvalidException thrown = assertThrows(ObjectIsInvalidException.class,
                () -> tagServiceMocked.createTag(tag));
        assertEquals("Tag with name = " + null + " is invalid", thrown.getMessage());

    }
    @Test
    void createTagSuccess() {
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        when(tagRepositoryMocked.isTagExists(TEST_TAG)).thenReturn(false);
        when(tagRepositoryMocked.createTag(tag)).thenReturn(true);
        assertTrue(tagServiceMocked.createTag(tag));

    }

    @Test
    void getAllTagsSuccess() {
        List<Tag> tags = List.of(new Tag(),new Tag(),new Tag());
        when(tagRepositoryMocked.getAllTags(1,10)).thenReturn(tags);
        assertEquals(tagServiceMocked.getAllTags(1,10),tags);
    }

    @Test
    void getTagByIdNoSuchItem() {
        when(tagRepositoryMocked.getTagByID(ID1)).thenReturn(null);
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.getTagById(ID1));
        assertEquals("Tag with id = " + ID1 + "doesn't exist", thrown.getMessage());

    }
    @Test
    void getTagByIdSuccess() {
        Tag tag = new Tag();
        when(tagRepositoryMocked.getTagByID(ID1)).thenReturn(tag);

        assertEquals(tag,tagServiceMocked.getTagById(ID1));
    }

    @Test
    void deleteTagSuccess() {
        when(tagRepositoryMocked.deleteTagByID(ID1)).thenReturn(true);
        assertTrue(tagServiceMocked.deleteTag(ID1));
    }
    @Test
    void deleteTagNoSuchItem() {
        when(tagRepositoryMocked.deleteTagByID(ID1)).thenReturn(false);

        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.deleteTag(ID1));
        assertEquals("There is no tag with id= " + ID1, thrown.getMessage());

    }

    @Test
    void getTagsIDSuccess() {
        Tag tag = new Tag();

        when(tagRepositoryMocked.getTagID(tag)).thenReturn(ID1);
        assertEquals(ID1, tagServiceMocked.getTagsID(tag));
    }

    @Test
    void getTagsIds() {
        Tag tag1 = new Tag();
        tag1.setId(ID1);
        Tag tag2 = new Tag();
        tag2.setId(ID2);
        Tag tag3 = new Tag();
        tag3.setId(ID3);
        when(tagRepositoryMocked.getTagID(tag1)).thenReturn(ID1);
        when(tagRepositoryMocked.getTagID(tag2)).thenReturn(ID2);
        when(tagRepositoryMocked.getTagID(tag3)).thenReturn(ID3);

        assertEquals(List.of(ID1, ID2, ID3), tagServiceMocked.getTagsIds(List.of(tag1,tag2,tag3)));
    }



    @Test
    void isTagWithNameExistsSuccess() {
        when(tagRepositoryMocked.isTagExists(TEST_TAG)).thenReturn(true);
        assertTrue(tagServiceMocked.isTagWithNameExists(TEST_TAG));
    }
    @Test
    void getTagsByNames(){
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        when(tagRepositoryMocked.getTagByName(TEST_TAG)).thenReturn(tag);
        assertEquals(List.of(tag),tagServiceMocked.getTagsByNames(List.of(TEST_TAG)));
    }
    @Test
    void getTagsByNamesException(){
        NoSuchItemException thrown = assertThrows(NoSuchItemException.class,
                () -> tagServiceMocked.getTagsByNames(List.of("Name")));
    }
    @Test
    void getMostUsedTag(){
        Tag tag = new Tag();
        tag.setName(TEST_TAG);
        when(tagRepositoryMocked.getMostUsedTag()).thenReturn(tag);
        assertEquals(tag,tagServiceMocked.getMostUsedTag());
    }

}