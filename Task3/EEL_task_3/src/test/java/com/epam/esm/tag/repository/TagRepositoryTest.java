package com.epam.esm.tag.repository;

import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.AppQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest {

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
        tag.setName("TagName");
        boolean result = tagRepository.createTag(tag);
        assertTrue(result);
        Tag tagRes = tagRepository.getTagByName("TagName");
        assertEquals(tag.getName(), tagRes.getName());
    }

    @Test
    void getAllTags() {
        Tag tag1 = new Tag();
        tag1.setName("Tag1");
        tag1.setId(1L);
        Tag tag2 = new Tag();
        tag2.setName("Tag2");
        tag2.setId(2L);
        List<Tag> expectedTagList = List.of(tag1,tag2);
        List<Tag> actualTagList = tagRepository.getAllTags();

        //then
        assertEquals(expectedTagList, actualTagList);
    }

    @Test
    void getTagByID() {
        Tag tag1 = new Tag();
        tag1.setName("Tag1");
        tag1.setId(1L);
        assertEquals(tag1,tagRepository.getTagByID(1L));
    }



    @Test
    void isExistsGetTagsIdAndDeleteTagByID() {
        Tag tag = new Tag();
        tag.setName("Tag1");
        assertTrue(tagRepository.isTagExists("Tag1"));
        assertTrue(tagRepository.deleteTagByID(tagRepository.getTagsID(tag)));
        assertFalse(tagRepository.isTagExists("Tag1"));
    }

    @Test
    void getAllTagsByCertificateID() {
        Tag tag = new Tag();
        tag.setName("Tag1");
        tag.setId(1L);
        assertEquals(List.of(tag), tagRepository.getAllTagsByCertificateID(1L));
    }
    @AfterEach
    public void drop(){
        embeddedDatabase.shutdown();
    }
}