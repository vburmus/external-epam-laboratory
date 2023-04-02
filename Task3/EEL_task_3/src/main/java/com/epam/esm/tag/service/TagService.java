package com.epam.esm.tag.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;


    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public boolean existsByName(String tagName) {
        return tagRepository.existsByName(tagName);
    }

    @Transactional
    public Tag createTag(Tag tag) {
        if (!tagRepository.exists(Example.of(tag))) {
            if (ParamsValidation.isTagValid(tag)) {
                tagRepository.save(tag);
                return tag;
            }
            throw new ObjectIsInvalidException("Tag with name = " + tag.getName() + " is invalid");
        }
        throw new ObjectAlreadyExistsException("Tag with name = " + tag.getName() + " already exists");

    }

    @Transactional
    public List<Tag> checkTagsAndSaveIfDontExist(GiftCertificate giftCertificate) {
        List<Tag> tagsFromGc = giftCertificate.getTags();
        if (tagsFromGc != null) {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : tagsFromGc) {
                tags.add(isTagsExistOrElseCreate(tag));
            }
            return tags;
        }
        return Collections.emptyList();
    }

    @Transactional
    public Tag isTagsExistOrElseCreate(Tag tag) {
        if (tagRepository.exists(Example.of(tag))) {
            Optional<Tag> existingTag = tagRepository.findOne(Example.of(tag));
            existingTag.ifPresent(value -> tag.setId(value.getId()));
        } else
            tagRepository.save(tag);
        return tag;

    }

    public Page<Tag> getAllTags(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ParamsValidation.isListArentEmptyOrElseThrowNoSuchItem(tagRepository.findAll(pageRequest));
    }

    public Tag getTagById(long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) return tag.get();
        throw new NoSuchItemException("Tag with id = " + id + "doesn't exist");
    }

    public boolean deleteTag(long id) {
        if (!tagRepository.existsById(id))
            throw new NoSuchItemException("There is no tag with id= " + id);
        tagRepository.deleteById(id);
        return true;
    }

    public Tag getMostUsedTag() {
        return tagRepository.getMostUsedTag();
    }
}
