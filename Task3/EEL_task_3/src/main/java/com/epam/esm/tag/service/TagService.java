package com.epam.esm.tag.service;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.rmi.ServerException;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    public boolean createTag(Tag tag) throws ServerException {
        if (!tagRepository.isTagExists(tag.getName())) {
            return tagRepository.createTag(tag);
        } else {
            //TODO
            throw new ServerException("This tag has already existed");
        }
    }
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }
    public Tag getTagById(long id) throws Exception {
        Tag tag = tagRepository.getTagByID(id);
       if(!tag.equals(null))
           return tag;
        else
            throw new Exception("There are no tags with id= " + id);
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
}
