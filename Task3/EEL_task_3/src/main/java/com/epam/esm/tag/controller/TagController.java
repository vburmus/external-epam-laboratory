package com.epam.esm.tag.controller;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping({"/{page}",""})
    public ResponseEntity<?> showAllTags(@PathVariable(required = false) Optional<Integer> page) {
        return new ResponseEntity<>(Map.of("tags", tagService.getAllTags(ParamsValidation.isValidPage(page))), HttpStatus.OK);
    }

    @GetMapping("/search/byId/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of("tag", tagService.getTagById(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        tagService.createTag(tag);
        return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);

    }

}
