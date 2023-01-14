package com.epam.esm.tag.service;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public boolean createTag(Tag tag) {
        if (!tagRepository.isTagExists(tag.getName())) {
            return tagRepository.createTag(tag);
        } else {
            //TODO
            throw new Error("This tag has already existed");
        }
    }

    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    public Tag getTagById(long id) throws Exception {
        Tag tag = tagRepository.getTagByID(id);
        if (!tag.equals(null))
            return tag;
        else
            throw new Exception("There are no tags with id= " + id);
    }
    public List<Tag> getTagsByNames(List<String> names) throws Exception {
        List<Tag> tags = new ArrayList<>();
        for(String name :names) {
            Tag tag = tagRepository.getTagByName(name);
            if(tag!=null)
                tags.add(tag);
        }
        if (!tags.isEmpty())
            return tags;
        else
            throw new Exception("There are no tags with any of this names " + names.toArray());
    }

    public boolean deleteTag(long id) throws Exception {
        if (tagRepository.deleteTagByID(id)) {
            return true;
        }
        throw new Exception("There is no tag with id= " + id);
    }

    public long getTagsID(Tag tag) {
        return tagRepository.getTagsID(tag);
    }
    public List<Long> getTagsIds(List<Tag> tags) {
         return tags.stream().map(tag -> getTagsID(tag)).collect(Collectors.toList());
    }
    public boolean isTagsExistOrElseCreate(List<Tag> tags)  {
        for (Tag tag : tags) {
            if (!isTagWithNameExists(tag.getName())) {
                createTag(tag);
            }
        }
        return true;
    }

    private boolean isTagWithNameExists(String name) {
        return tagRepository.isTagExists(name);
    }
}
