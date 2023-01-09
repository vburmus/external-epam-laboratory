package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class TagRepositoryImpl implements TagRepository{
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createTag(Tag tag) {
        return jdbcTemplate.update(AppQuery.Tag.CREATE_TAG, tag.getName()) == 1 ;
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(AppQuery.Tag.GET_ALL_TAGS,new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag getTagByID(long id) {
        return jdbcTemplate.query(AppQuery.Tag.GET_TAG_BY_ID,new Object[]{id},new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public boolean deleteTagByID(long id) {
        return jdbcTemplate.update(AppQuery.Tag.DELETE_TAG, id) == 1;
    }
    @Override
    public boolean isTagExists(String tagName) {
        return jdbcTemplate.queryForObject(AppQuery.Tag.IS_TAG_EXISTS,Integer.class,new Object[]{tagName})==1;
    }
    @Override
    public long getTagsID(Tag tag) {
        return jdbcTemplate.queryForObject(AppQuery.Tag.GET_TAGS_ID,Long.class,new Object[]{tag.getName()});
    }

//TODO

    @Override
    public List<Tag> getAllTagsByCertificateID(long id) {
        return null;
    }
}
