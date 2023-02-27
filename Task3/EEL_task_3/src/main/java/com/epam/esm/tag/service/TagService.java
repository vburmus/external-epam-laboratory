package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.AppQuery;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;


    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public boolean createTag(Tag tag) {
        if (!tagRepository.isTagExists(tag.getName())) {
            if (ParamsValidation.isTagValid(tag)) return tagRepository.createTag(tag);
            throw new ObjectIsInvalidException("Tag with name = " + tag.getName() + " is invalid");
        }
        throw new ObjectAlreadyExistsException("Tag with name = " + tag.getName() + " already exists");

    }

    public List<Tag> getAllTags(Integer page,Integer size) {
        return tagRepository.getAllTags(page,size);
    }

    public Tag getTagById(long id) {
        Tag tag = tagRepository.getTagByID(id);
        if (tag != null) return tag;
        throw new NoSuchItemException("Tag with id = " + id + "doesn't exist");
    }

    public List<Tag> getTagsByNames(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            Tag tag = tagRepository.getTagByName(name);
            if (tag != null) tags.add(tag);
        }
        if (!tags.isEmpty()) return tags;
        else throw new NoSuchItemException("There are no tags with any of this names " + names.toArray());
    }

    public boolean deleteTag(long id) {
        if (tagRepository.deleteTagByID(id)) {
            return true;
        }
        throw new NoSuchItemException("There is no tag with id= " + id);
    }

    public long getTagsID(Tag tag) {
        return tagRepository.getTagsID(tag);
    }

    public List<Long> getTagsIds(List<Tag> tags) {
        return tags.stream().map(this::getTagsID).collect(Collectors.toList());


    }
    @Transactional
    public boolean isTagsExistOrElseCreate(List<Tag> tags) {
        for (Tag tag : tags) {
            if (!isTagWithNameExists(tag.getName())) {
                createTag(tag);
            }
        }
        return true;
    }

    public boolean isTagWithNameExists(String name) {
        return tagRepository.isTagExists(name);
    }

    public List<Tag> getAllTagsByCertificateID(long id) {
        return tagRepository.getAllTagsByCertificateID(id);
    }

    public Tag getMostUsedTag(){
        return tagRepository.getMostUsedTag();
    }
}
