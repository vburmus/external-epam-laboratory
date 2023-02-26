package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;

import java.util.List;

public interface TagRepository {

    boolean createTag(Tag tag);

    List<Tag> getAllTags(Integer page);

    Tag getTagByID(long id);

    List<Tag> getAllTagsByCertificateID(long id);

    long getTagsID(Tag tag);

    Tag getTagByName(String name);

    boolean deleteTagByID(long id);

    boolean isTagExists(String tagName);

}
