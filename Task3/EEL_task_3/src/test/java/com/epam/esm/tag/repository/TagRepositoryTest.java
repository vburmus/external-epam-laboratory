package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest {

    public static final String TAG_NAME = "TagName";
    public static final String TAG_1 = "Tag1";
    public static final long ID = 1L;
    public static final long ID1 = 2L;
    public static final String TAG_2 = "Tag2";
    private TagRepository tagRepository;
    private EmbeddedDatabase embeddedDatabase;

    @BeforeEach
    public void init(){
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("embeddedDB/task_3.sql")
                .addScript("embeddedDB/insert-some-data.sql")
                .build();
        this.tagRepository = new TagRepositoryImpl(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    void createTagGetTagByName() {
        Tag tag = new Tag();
        tag.setName(TAG_NAME);
        boolean result = tagRepository.createTag(tag);
        assertTrue(result);
        Tag tagRes = tagRepository.getTagByName(TAG_NAME);
        assertEquals(tag.getName(), tagRes.getName());
    }

    @Test
    void getAllTags() {
        Tag tag1 = new Tag();
        tag1.setName(TAG_1);
        tag1.setId(ID);
        Tag tag2 = new Tag();
        tag2.setName(TAG_2);
        tag2.setId(ID1);
        List<Tag> expectedTagList = List.of(tag1,tag2);
        List<Tag> actualTagList = tagRepository.getAllTags(1,10);

        //then
        assertEquals(expectedTagList, actualTagList);
    }

    @Test
    void getTagByID() {
        Tag tag1 = new Tag();
        tag1.setName(TAG_1);
        tag1.setId(ID);
        assertEquals(tag1,tagRepository.getTagByID(ID));
    }



    @Test
    void isExistsGetTagsIdAndDeleteTagByID() {
        Tag tag = new Tag();
        tag.setName(TAG_1);
        assertTrue(tagRepository.isTagExists(TAG_1));
        assertTrue(tagRepository.deleteTagByID(tagRepository.getTagID(tag)));
        assertFalse(tagRepository.isTagExists(TAG_1));
    }

    @Test
    void getAllTagsByCertificateID() {
        Tag tag = new Tag();
        tag.setName(TAG_2);
        tag.setId(ID1);
        assertEquals(List.of(tag), tagRepository.getAllTagsByCertificateID(ID1));
    }
    @Test
    void getMostUsedTag(){
        Tag t = new Tag();
        t.setId(ID1);
        t.setName("Tag2");
        assertEquals(t,tagRepository.getMostUsedTag());
    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}